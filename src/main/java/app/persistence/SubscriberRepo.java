package app.persistence;

import app.exceptions.DatabaseException;
import app.models.Subscriber;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubscriberRepo {

    public static Subscriber insertNewsletterSignUp(ConnectionPool connectionPool, Subscriber subscriber) throws DatabaseException {

        String query = "INSERT INTO subscribers(email, signup_date) VALUES(?,?)";

        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, subscriber.getEmail());
                preparedStatement.setDate(2, Date.valueOf(subscriber.getSignUp_date())); // Convert LocalDate to SQL Date

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DatabaseException("Insert failed, no rows affected");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not insert new sign-up " + e);
        }
        return subscriber;
    }
}
