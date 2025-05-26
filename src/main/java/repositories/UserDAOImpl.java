package repositories;

import models.User;
import util.ConnectionUtil;

import java.sql.*;
import java.util.Properties;


/*
* What is JDBC?
*   Java Database Connectivity
*       - Allows us to connect to a database and run sql commands
*
*
* What is needed to connect to our postgresql database on AWS using JDBC?
*   - url (location of our database)
*       - syntax: jdbc:postgresql://[AWS_RDS_ENDPOINT]/[DATABASE_NAME]
*   - username/password for our aws database
*   - driver
*
*
*   Interfaces and classes of JDBC:
*   - Connection (interface) : active connection with the database
*   - DriverManager : retrieves the connection from our database
*   - Statement : object representation of the sql statement (does NOT prevent SQL injection)
*       - PreparedStatement : object representation of the sql statement (prevents SQL injection)
*   - ResultSet : object representation of the result set
*
*/


public class UserDAOImpl implements UserDAO {

    String url = "jdbc:postgresql://" + System.getenv("AWS_RDS_ENDPOINT") + ":5432/grocerylist";
    String username = System.getenv("RDS_USERNAME");
    String password = System.getenv("RDS_PASSWORD");

    @Override
    public User getUserGivenUsername(String username) {
        User user = null;

        try {
            // Explicitly load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(this.url, this.username, this.password)) {
                String sql = "SELECT * FROM users WHERE user_username = ?;";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, username);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            user = new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                                    rs.getString(4), rs.getString(5), rs.getTimestamp(6));
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void createUser(User user) {
        try{
            Connection conn = ConnectionUtil.getConnection();

            String sql = "insert into users (user_username, user_password, user_firstname, user_lastname) values (?, ?, ?, ?);";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getLastname());

            ps.executeUpdate();

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

    }
}
