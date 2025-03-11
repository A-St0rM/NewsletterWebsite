package app;

import app.controller.AdminController;
import app.exceptions.DatabaseException;
import app.persistence.AdminRepo;
import app.service.AdminService;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

public class Main {

    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/NewsletterDB?currentSchema=public";
    private static DatabaseConnecter databaseConnecter;

    static {
        try {
            databaseConnecter = new DatabaseConnecter(URL, USERNAME, PASSWORD);
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create necessary objects and pass them to constructors
        AdminRepo adminRepo = new AdminRepo(databaseConnecter);
        AdminService adminService = new AdminService(adminRepo);

        // Initializing Javalin and Jetty webserver with custom configuration
        Javalin app = Javalin.create(config -> {
            // Configure static file serving (e.g., CSS, JS, etc.)
            config.staticFiles.add("/public");

            // Configure session handling
            config.jetty.modifyServletContextHandler(handler ->
                    handler.setSessionHandler(SessionConfig.sessionConfig())
            );

            // Configure Thymeleaf template rendering
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Initialize Controllers with the Javalin app and services
        new AdminController(app, adminService);

        // Additional routes or configurations can be added here
        app.get("/", ctx -> ctx.render("index.html"));
    }
}
