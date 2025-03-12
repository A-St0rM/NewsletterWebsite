package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.models.Admin;
import app.persistence.AdminRepo;
import app.persistence.ConnectionPool;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "NewsletterDB";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing

        app.get("/", ctx -> ctx.render("index.html"));
        app.post("login", ctx -> ctx.render("dashboard.html"));
        app.get("/signIn", ctx -> ctx.render("signIn.html"));


        List<Admin> admins = AdminRepo.getAlladmins(connectionPool);
        admins.forEach(System.out::println);
    }
}
