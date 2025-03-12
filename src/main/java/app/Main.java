package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controller.SubscriberController;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.HashMap;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "NewsletterDB";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);
    private static final SubscriberController subscriberController = new SubscriberController();

    public static void main(String[] args) {
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

        app.post("/newsletter/signup", ctx -> subscriberController.signUp(ctx, connectionPool));
    }
}
