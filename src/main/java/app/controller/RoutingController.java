package app.controller;

import app.Main;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.models.Newsletter;
import app.persistence.ConnectionPool;
import app.persistence.NewsletterRepo;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.List;
import java.util.logging.Logger;

public class RoutingController {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "NewsletterDB";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    private static final SubscriberController subscriberController = new SubscriberController(connectionPool);
    private static final AdminController adminController = new AdminController(connectionPool);
    private static final NewsletterController newsletterController = new NewsletterController(connectionPool);


    public static void initalizeRouting(){

        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/pdf";   // Serve at http://localhost:7000/files
                staticFiles.directory = "pdf";    // Serve from "files" folder in working directory
                staticFiles.location = Location.EXTERNAL; // Load from outside the JAR
            });
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        app.get("/", ctx -> {
            List<Newsletter> newsletters = NewsletterRepo.getAllNewsletters(connectionPool);

            ctx.attribute("newsletters", newsletters);

            ctx.attribute("message", ctx.sessionAttribute("message"));
            ctx.attribute("error", ctx.sessionAttribute("error"));

            // Fjern session attributes efter brug
            ctx.sessionAttribute("message", null);
            ctx.sessionAttribute("error", null);

            ctx.render("index.html");
        });


        //API Endpoints
        app.post("/newsletter/signup", ctx -> subscriberController.subscribe(ctx));
        app.post("/admin/signIn", ctx -> adminController.SignInAsAdmin(ctx));
        app.post("/admin/addNewsletter", ctx -> newsletterController.addNewsletter(ctx));

        //Web endpoints
        app.get("/signIn", ctx -> {
            ctx.attribute("error", ctx.sessionAttribute("error"));
            ctx.sessionAttribute("error", null); // Nulstil error efter visning
            ctx.render("signIn.html");
        });


        app.get("/admin/signIn", ctx -> ctx.redirect("/signIn"));
        app.get("/admin/addNewsletter", ctx -> ctx.render("dashboard.html"));





    }

}
