package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Class to query country table.
 * */
public class CountryDao {

    /**
     * List created to hold the countries data from the database countries table.
     * */
    public static ObservableList<Country> countryList = FXCollections.observableArrayList();

    /**
     * Method to query the countries table for the country list.
     * */
    public static ObservableList<Country> getAllCountries(){
        String sql = "SELECT * FROM countries";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int countryId = rs.getInt("Country_ID");
                String country = rs.getString("Country");
                Country c = new Country(countryId, country);
                countryList.add(c);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return countryList;
    }

}
