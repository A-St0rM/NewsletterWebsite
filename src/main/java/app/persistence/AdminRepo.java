package app.persistence;

import app.DatabaseConnecter;
import app.exceptions.DatabaseException;
import app.model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRepo {

    private final DatabaseConnecter databaseConnecter;

    // Constructor injection of DatabaseConnecter
    public AdminRepo(DatabaseConnecter databaseConnecter) {
        this.databaseConnecter = databaseConnecter;
    }

    public void addAdmin(Admin admin) throws DatabaseException {
        String query = "INSERT INTO admins (userName, password) VALUES (?, ?)";
        try (Connection connection = databaseConnecter.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, admin.getUserName());
            preparedStatement.setString(2, admin.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Could not add admin: " + e.getMessage());
        }
    }

    public boolean deleteAdmin(int id) throws DatabaseException {
        String query = "DELETE FROM admins WHERE admin_id = ?";
        boolean result = false;
        try (Connection connection = databaseConnecter.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                result = true;
            } else {
                throw new DatabaseException("No admin found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete admin with ID: " + e.getMessage());
        }
        return result;
    }

    public boolean validateAdmin(String username, String password) throws DatabaseException {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try (Connection connection = databaseConnecter.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If record exists, login is valid
        } catch (SQLException e) {
            throw new DatabaseException("Could not validate admin: " + e.getMessage());
        }
    }
}
