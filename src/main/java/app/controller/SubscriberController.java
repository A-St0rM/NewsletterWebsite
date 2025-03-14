package app.controller;

import app.exceptions.DatabaseException;
import app.models.Subscriber;
import app.persistence.ConnectionPool;
import app.persistence.SubscriberRepo;
import io.javalin.http.Context;


public class SubscriberController {

    private static ConnectionPool connectionPool;

    public SubscriberController(ConnectionPool connectionPool){
        this.connectionPool = connectionPool;
    }


    public void subscribe(Context ctx) throws DatabaseException {
        String email = ctx.formParam("email");
        String message = "";
        if (email != null) {
            int result = SubscriberRepo.subscribe(email,connectionPool);
            if (result == 1) {
                message = "Tak for din tilmelding";
            } else if (result == 0) {
                message = "Tak, men du var allerede tilmeldt";
            }
            ctx.attribute("message", message);
            ctx.render("index.html");
        }
    }
}
