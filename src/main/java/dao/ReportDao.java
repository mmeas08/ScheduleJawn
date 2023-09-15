package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class holds the queries made for obtaining data for the reports section.
 * */
public class ReportDao {

    /**
     * List to hold data of the number of appts per month.
     * */
    public static ObservableList<Report> monthReportList = FXCollections.observableArrayList();

    /**
     * Method to query the database for the number of appts per month.
     * */
    public static ObservableList<Report> getMonthReport(){
        String sql = "SELECT MONTHNAME(Start) AS 'Month', COUNT(MONTHNAME(Start)) AS 'Appt_Count' " +
                        "FROM appointments GROUP BY MONTHNAME(Start) ORDER BY Month";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String month = rs.getString("Month");
                int appointmentCount = rs.getInt("Appt_Count");
                Report m = new Report(month, appointmentCount);
                monthReportList.add(m);
            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return monthReportList;
    }

    /**
     * List to hold data of the number of appointments per type.
     * */
    public static ObservableList<Report> typeReportList = FXCollections.observableArrayList();

    /**
     * Method to query the data for the number of appts per type.
     * */
    public static ObservableList<Report> getTypeReport(){
        String sql = "SELECT Type, COUNT(Type) AS 'Type_Count' FROM appointments GROUP BY Type";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String type = rs.getString("Type");
                int appointmentCount = rs.getInt("Type_Count");
                Report t = new Report(type, appointmentCount);
                typeReportList.add(t);
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return typeReportList;
    }

    /**
     * List to hold the data of the number of customers per country.
     * */
    public static ObservableList<Report> countryReportList = FXCollections.observableArrayList();

    /**
     * Method to query database for the number of customers per country.
     * */
    public static ObservableList<Report> getCountryReport(){
        String sql = "SELECT countries.Country, COUNT(Customer_ID) AS 'Total_Customers' FROM customers " +
                "INNER JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID " +
                "GROUP BY countries.Country ORDER BY Total_Customers DESC";
        try{
            PreparedStatement ps =JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String country = rs.getString("Country");
                int totalCustomers = rs.getInt("Total_Customers");
                Report c = new Report(country, totalCustomers);
                countryReportList.add(c);
            }
        }catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return countryReportList;
    }


}
