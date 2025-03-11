package app.controller;

import app.exceptions.DatabaseException;
import app.model.Admin;
import app.service.AdminService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    private final AdminService adminService;

    // Constructor injection of AdminService
    public AdminController(Javalin app, AdminService adminService) {
        this.adminService = adminService;
        configureRoutes(app);
    }

    private void configureRoutes(Javalin app) {
        app.post("/admin", this::addNewAdmin);
        app.post("/admin/login", this::handleAdminLogin);
    }

    private void addNewAdmin(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        Admin admin = new Admin(username, password);
        try {
            adminService.addAdmin(admin);
            ctx.status(201).json("{\"message\": \"Admin added successfully\"}");
        } catch (DatabaseException e) {
            ctx.status(500).json("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private void handleAdminLogin(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        boolean isAuthenticated = adminService.validateAdmin(username, password);

        if (isAuthenticated) {
            ctx.status(200).json("{\"message\": \"Login successful\"}");
        } else {
            ctx.status(401).json("{\"error\": \"Invalid credentials\"}");
        }
    }
}
