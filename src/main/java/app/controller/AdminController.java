package app.controller;

import app.exceptions.DatabaseException;
import app.models.Admin;
import app.persistence.AdminRepo;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.util.List;

public class AdminController {

    private ConnectionPool connectionPool;

    public AdminController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }

    public void SignInAsAdmin(Context ctx) {

        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        Admin admin = new Admin(username, password);

        try {
            List<Admin> adminsList = AdminRepo.getAlladmins(connectionPool);
            boolean loginSuccess = false;

            for (Admin a : adminsList) {
                if (a.getUsername().equals(admin.getUsername()) && a.getPassword().equals(admin.getPassword())) {
                    ctx.render("dashboard.html");
                    loginSuccess = true;
                    break;
                }
            }

            if (!loginSuccess) {
                ctx.sessionAttribute("error", "Wrong username or password");
                ctx.redirect("/signIn");
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to get admins from DB " + e);
        }
    }

}

