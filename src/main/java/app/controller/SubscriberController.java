package app.controller;

import app.exceptions.DatabaseException;
import app.models.Subscriber;
import app.persistence.ConnectionPool;
import app.persistence.SubscriberRepo;
import io.javalin.http.Context;
import java.time.LocalDate;

public class SubscriberController {

    public void signUp(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");

        if (email == null || email.trim().isEmpty()) {
            ctx.sessionAttribute("error", "Email er påkrævet.");
            ctx.redirect("/");
            return;
        }

        try {
            Subscriber subscriber = new Subscriber(email, LocalDate.now());
            SubscriberRepo.insertNewsletterSignUp(connectionPool, subscriber);

            ctx.sessionAttribute("message", "Tilmelding succesfuld!");
        } catch (DatabaseException e) {
            ctx.sessionAttribute("error", "Noget gik galt. Prøv igen.");
        }

        ctx.redirect("/");
    }
}
