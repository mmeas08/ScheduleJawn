package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;
import model.CustomerPlus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Class contains the query made to obtain data for the customer table in the application.
 * */
public class CustomerPlusDao {

    /**
     * List to hold the data extracted from the database joined tables - customer, firstleveldivison and countries.
     * */
    public static ObservableList<CustomerPlus> customerPlusTableList = FXCollections.observableArrayList();
    public static ObservableList<CustomerPlus> getCustomerPlusTable(){
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address,  customers.Postal_Code, customers.Phone, first_level_divisions.Division, countries.Country FROM customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String division = rs.getString("Division");
                String country = rs.getString("Country");
                String phone = rs.getString("Phone");
                CustomerPlus D = new CustomerPlus(customerId, customerName, customerAddress, postalCode, division, country, phone);
                customerPlusTableList.add(D);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customerPlusTableList;
    }

    /**
     * Method to clear and extract the updated information from the database for the application customer table.
     * */
    public static ObservableList<CustomerPlus> refreshCustomerPlusTableList(){
        customerPlusTableList.clear();
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address,  customers.Postal_Code, customers.Phone, first_level_divisions.Division, countries.Country FROM customers INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String division = rs.getString("Division");
                String country = rs.getString("Country");
                String phone = rs.getString("Phone");
                CustomerPlus D = new CustomerPlus(customerId, customerName, customerAddress, postalCode, division, country, phone);
                customerPlusTableList.add(D);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return customerPlusTableList;
    }



}
