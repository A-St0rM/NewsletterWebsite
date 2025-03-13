package app.controller;

import app.exceptions.DatabaseException;
import app.models.Admin;
import app.persistence.AdminRepo;
import app.persistence.ConnectionPool;
import io.javalin.http.Context;

import java.util.List;

public class AdminController {


    public Admin SignInAsAdmin(Context ctx, ConnectionPool connectionPool) throws DatabaseException{

        String username = ctx.formParam("username");
        String password = ctx.formParam("password");

        Admin admin = new Admin(username, password);

        try{
            List<Admin> adminsList = AdminRepo.getAlladmins(connectionPool);
            for(Admin a : adminsList){
                if(a.getUsername().equals(admin.getUsername()) && a.getPassword().equals(admin.getPassword())){
                    ctx.render("dashboard.html");
                }
            }
        } catch (Exception e) {
            throw new DatabaseException("Failed to get admins from DB " + e);
        }
        return admin;
    }

}
