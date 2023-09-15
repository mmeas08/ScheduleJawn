package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class holds the queries for user data.
 * */
public class UserDao {

    /**
     * List holds the data of database user table.
     * */
    public static ObservableList<User> userList = FXCollections.observableArrayList();

    /**
     * Query for user data into the userList.
     * */
    public static ObservableList<User> getAllUsers(){
        try{
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int userId = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");
                User U = new User(userId, username, password);
                userList.add(U);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return userList;
    }



    /**
     * Determine if username and password are valid for access.
     *
     * @param username
     * @param password
     * @return true or false
     * */
    public static boolean userAccessOrNaw(String username, String password){
        try{
            String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                System.out.println("Found user access at DB");
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("no user access at DB");
        return false;
    }
}
