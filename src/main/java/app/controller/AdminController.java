package app.controller;

import app.exceptions.DatabaseException;
import app.model.Admin;
import app.persistence.AdminRepo;
import app.persistence.ConnectionPool;
import app.service.AdminService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool));
    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        //Hent form parametre
        String username = ctx.formParam("username"); // "username" og "password" skal stemme overens med navngivning af attributter i html-fil!
        String password = ctx.formParam("password");


        //Check om bruger findes i DB
        //Hvis ja: Send bruger videre til task-siden
        try {
            Admin admin = AdminRepo.login(username, password, connectionPool);
            ctx.sessionAttribute("currentUser", admin); //sørger for at man er logget på så længe browserens session er på
            ctx.render("dashboard.html");
        } catch (DatabaseException e) {
            //Hvis nej: Send tilbage til forside med fejl!
            ctx.attribute("message", e.getMessage());
            ctx.render("index.html");
        }

    }
}
