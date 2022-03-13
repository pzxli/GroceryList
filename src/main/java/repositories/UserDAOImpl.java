package repositories;

import models.User;

import javax.xml.transform.Result;
import java.sql.*;

/*
What is JDBC?
        Java DataBase Connectivity
            - allows us to connect to a database and run sql commands

What is needed to connect to our postgresql database on AWS using JDBC?
    - url (location of our database)
        - syntax: jdbc:postgres://[AWS_RDS_ENDPOINT]/[DATABASE_NAME]
    - username for our aws database
    - password for our aws database
    - driver

    Interfaces and classes of JDBC:
    - Connection (interface) : active connection with the database
    - DriverManager : retrieves the connection from our database
    - Statement : object representation of the sql statement (does NOT prevent SQL injection)
        - PreparedStatement : object representation of the sql statement (prevents SQL injection)
    - ResultSet : object representation of the result set

The most common error connecting to JDBC is
    no suitable driver found
        - you dont have the driver
        - url string is not correct
 */

public class UserDAOImpl implements UserDAO{
    String url = "jdbc:postgresql://pl-fsj-db.ctyc5nciwub0.us-east-1.rds.amazonaws.com/grocerylist";
    String username = "postgres";
    String password = "p4ssw0rd";

    @Override
    public User getUserGivenUsername(String username) {
        User user = null;

        try (Connection conn = DriverManager.getConnection(this.url, this.username, this.password)){

            String sql = "SELECT * FROM users WHERE user_username = ?;";

            //preparing our sql statement
            PreparedStatement ps = conn.prepareStatement(sql);

            //we are adding the username into the question mark in the sql statement
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getTimestamp(6));
            }

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return user;
    }

    @Override
    public void createUser(User user) {

        try (Connection conn = DriverManager.getConnection(this.url, this.username, this.password)){

            String sql = "insert into users (user_username, user_password, user_firstname, user_lastname) values (?, ?, ?, ?);";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getLastname());

            ps.executeUpdate();

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
