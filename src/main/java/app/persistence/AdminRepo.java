package app.persistence;

import app.exceptions.DatabaseException;
import app.models.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepo {

    public static List<Admin> getAlladmins(ConnectionPool connectionPool) throws DatabaseException
    {
        List<Admin> adminList = new ArrayList<>();
        String sql = "SELECT * FROM admins";
        try (Connection connection = connectionPool.getConnection())
        {
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ResultSet rs = ps.executeQuery();

                while (rs.next())
                {
                    String userName = rs.getString("username");
                    String password = rs.getString("password");
                    Admin admin = new Admin(userName, password);
                    adminList.add(admin);
                }
            }
        }
        catch (SQLException ex)
        {
            throw new DatabaseException("Could not get users from database " + ex );
        }
        return adminList;
    }

}
