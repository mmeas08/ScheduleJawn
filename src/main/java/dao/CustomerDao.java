package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Class to hold the queries made from customers table.
 * */
public class CustomerDao {

    /**
     * List to hold customers data from customers table.
     * */
    public static ObservableList<Customer> customerList = FXCollections.observableArrayList();

    /**
     * Method queries the database customer table for the customer list.
     * */
    public static ObservableList<Customer> getAllCustomers(){
        String sql = "SELECT * FROM customers";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Customer C = new Customer(customerId, customerName, customerAddress, postalCode, phone, divisionId);
                customerList.add(C);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customerList;
    }

    /**
     * Method to clear existing customer list and extract into the list new data from database customers table.
     * */
    public static ObservableList<Customer> refreshCustomerList(){
        customerList.clear();
        String sql = "SELECT * FROM customers";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Customer C = new Customer(customerId, customerName, customerAddress, postalCode, phone, divisionId);
                customerList.add(C);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customerList;
    }


    /**
     * Method to insert a new row into the database customers table.
     * */
    public static int insertAddCustomer(int customerId, String customerName, String customerAddress,
                                        String postalCode, String phone, LocalDateTime createDate, String createdBy,
                                        LocalDateTime lastUpdate, String lastUpdatedBy, int divisionId) throws SQLException {
        String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Division_ID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, customerId);
        ps.setString(2, customerName);
        ps.setString(3, customerAddress);
        ps.setString(4, postalCode);
        ps.setString(5, phone);
        ps.setTimestamp(6, Timestamp.valueOf(createDate));
        ps.setString(7, createdBy);
        ps.setTimestamp(8, Timestamp.valueOf(lastUpdate));
        ps.setString(9, lastUpdatedBy);
        ps.setInt(10, divisionId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Method to update a selected customer with use of customerID.
     * */
    public static int updateEditCustomer(int customerId, String customerName, String customerAddress,
                                         String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "UPDATE customers "
                + "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? "
                + "WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, customerAddress);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, divisionId);
        ps.setInt(6, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Method to delete customer from the database customer table.
     * */
    public static int deleteCustomer(int selectedCustomerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, selectedCustomerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

}
