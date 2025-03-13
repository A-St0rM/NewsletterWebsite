package app.controller;

import app.Main;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.logging.Logger;

public class RoutingController {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "NewsletterDB";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    private static final SubscriberController subscriberController = new SubscriberController();
    private static final AdminController adminController = new AdminController(connectionPool);


    public static void initalizeRouting(){

        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        app.get("/", ctx -> {
            ctx.attribute("message", ctx.sessionAttribute("message"));
            ctx.attribute("error", ctx.sessionAttribute("error"));

            // Fjern session attributes efter brug
            ctx.sessionAttribute("message", null);
            ctx.sessionAttribute("error", null);

            ctx.render("index.html");
        });


        //API Endpoints
        app.post("/newsletter/signup", ctx -> subscriberController.signUp(ctx, connectionPool));
        app.post("/admin/signIn", ctx -> adminController.SignInAsAdmin(ctx));

        //Web endpoints
        app.get("/signIn", ctx -> {
            ctx.attribute("error", ctx.sessionAttribute("error"));
            ctx.sessionAttribute("error", null); // Nulstil error efter visning
            ctx.render("signIn.html");
        });

        app.get("/archives", ctx -> ctx.render("archives.html"));
        app.get("/admin/signIn", ctx -> ctx.redirect("/signIn"));


    }

}
