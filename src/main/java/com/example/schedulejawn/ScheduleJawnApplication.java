package com.example.schedulejawn;

import dao.*;
import helper.JDBC;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/** The class creates the application for ScheduleJawn, a scheduling management tool.
 *
 */
public class ScheduleJawnApplication extends Application {

    /**
     * The method loads the login screen of the application.
     * @param stage
     *
     * */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        stage.setScene(new Scene(root, 1000,700));
        stage.show();
    }


    /**
     * The main method starts connection with database, retrieve the data from the database, and launches the application.
     * It also closes the DB connection once application is terminated.
     * @param args
     * */
    public static void main(String[] args) throws IOException {

        JDBC.startConnection();

        CustomerDao.getAllCustomers();
        AppointmentDao.getAllAppointment();
        CountryDao.getAllCountries();
        FirstLevelDivisionDao.getFirstLevelDivision();
        ContactDao.getAllContact();
        UserDao.getAllUsers();
        CustomerPlusDao.getCustomerPlusTable();
        AppointmentDao.getAppointmentInFifteen();
        ReportDao.getCountryReport();
        ReportDao.getMonthReport();
        ReportDao.getTypeReport();

        AppointmentDao.getApptCurrentWeek();
        ObservableList<Appointment> aList = AppointmentDao.viewCurrentWeekList;
        for(Appointment a : aList){
            System.out.println("Week: " + a.getAppointmentId() + " || " + a.getCustomerId());
        }

        AppointmentDao.getApptCurrentMonth();
        ObservableList<Appointment> bList = AppointmentDao.viewCurrentMonthList;
        for(Appointment b : bList){
            System.out.println("Month: " + b.getAppointmentId() + " || " + b.getCustomerId());
        }

        String filename = "README.txt";
        PrintWriter outputFile = new PrintWriter(filename);
        outputFile.println("Title: ScheduleJawn");
        outputFile.println("Purpose: To provide a business tool to stay organize with customers and appointments");
        outputFile.println("Application Version: 1.0 || Date: 08/28/2023");
        outputFile.println("IntelliJ Community 2023.2 || Java Version 17.0.8 || JavaFX 17.0.6");
        outputFile.println("How to run the program: Establish a connection with MySQl Server. " +
                "\nOpen the program in IntelliJ. Run the project.");
        outputFile.println("MySQL Connector driver version: mysql-connector-j-8.1.0");
        outputFile.close();

        launch();
        JDBC.closeConnection();
    }

}