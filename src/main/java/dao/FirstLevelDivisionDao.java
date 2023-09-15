package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that holds all the queries specific to firstleveldivision.
 * */
public class FirstLevelDivisionDao {

    /**
     * List to hold the data of firstleveldivsion.
     * */
    public static ObservableList<FirstLevelDivision> firstLevelDivisionList = FXCollections.observableArrayList();

    /**
     * Method to extract data from the database firstleveldivision table for the firstLevelDivisionList.
     * */
    public static ObservableList<FirstLevelDivision> getFirstLevelDivision(){
        String sql = "SELECT * FROM first_level_divisions";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int divisionId = rs.getInt("Division_ID");
                int countryId = rs.getInt("Country_ID");
                String division = rs.getString("Division");
                FirstLevelDivision F = new FirstLevelDivision(divisionId, countryId, division);
                firstLevelDivisionList.add(F);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return firstLevelDivisionList;
    }

    /**
     * Method to determine the firstleveldivisions based on countryID.
     *
     * @param selectedCountryId
     * */
    public static ObservableList<FirstLevelDivision> filteredByCountry(int selectedCountryId){
        ObservableList<FirstLevelDivision> filteredByCountryList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, selectedCountryId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int divisionId = rs.getInt("Division_ID");
                int countryId = rs.getInt("Country_ID");
                String division = rs.getString("Division");
                FirstLevelDivision F = new FirstLevelDivision(divisionId, countryId, division);
                filteredByCountryList.add(F);
            }
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return filteredByCountryList;
    }



}
