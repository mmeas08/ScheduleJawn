package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;


/** Class contains methods that query the appointment table in the database.
 *
 * */
public class AppointmentDao {

    /** Appointment list for the appointment table.
     * */
    public static ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    /** Method to extract data from the database appointments table for the appointmentList.
     *
     * */
    public static ObservableList<Appointment> getAllAppointment(){
        String sql = "SELECT * FROM appointments";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                appointmentList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return appointmentList;
    }

    /** Method to insert a new row into the database appointments table.
     * */
    public static int insertAddAppt(int appointmentId, String title, String description,
                                    String location, String type, Timestamp start, Timestamp end,
                                    Timestamp createDate, String createdBy, Timestamp lastUpdate,
                                    String lastUpdatedBy, int customerId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, " +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, appointmentId);
        ps.setString(2, title);
        ps.setString(3, description);
        ps.setString(4, location);
        ps.setString(5, type);
        ps.setTimestamp(6, start);
        ps.setTimestamp(7, end);
        ps.setTimestamp(8, createDate);
        ps.setString(9, createdBy);
        ps.setTimestamp(10, lastUpdate);
        ps.setString(11, lastUpdatedBy);
        ps.setInt(12, customerId);
        ps.setInt(13, userId);
        ps.setInt(14, contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** Method created to update the selected appointment.
     * */
    public static int updateEditAppt(int appointmentId, String title, String description,
                                     String location, String type, Timestamp start, Timestamp end,
                                     int customerId, int userId, int contactId) throws SQLException {
        String sql = "UPDATE appointments "
                + "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? "
                + "WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, appointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** Method created to delete selected appointments from the database.
     *
     * @param selectedAppointmentId selected appointment ID
     * */
    public static int deleteAppointment(int selectedAppointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, selectedAppointmentId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** Method to determine if customer is safe to delete by not having any appts.
     *
     * @param selectedCustomerId selected customer ID
     * */
    public static boolean doesCustomerHaveAppointment(int selectedCustomerId) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, selectedCustomerId);
        ResultSet rs = ps.executeQuery();
        if(rs.isBeforeFirst()){
            System.out.println("Found appointment for customer");
            return true;
        }
        System.out.println("Customer has no appointment");
        return false;
    }

    /** Method to determine if selected appt does not overlap.
     *
     * @param selectedCustomerId
     * @param endHour
     * @param selectedDate
     * @param startHour
     * */
    public static ObservableList<Appointment> selectedCustomerAndAppt(int selectedCustomerId, Date selectedDate, int startHour, int endHour ){
        ObservableList<Appointment> selectedCustomerApptList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ? AND DATE(Start) = ? AND HOUR(Start) BETWEEN ? AND ?";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, selectedCustomerId);
            ps.setDate(2, selectedDate);
            ps.setInt(3, startHour);
            ps.setInt(4, endHour);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                selectedCustomerApptList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return selectedCustomerApptList;
    }

    /** Method to clear appointment list and extract the new data.
     * */
    public static ObservableList<Appointment> refreshAppointmentList(){
        appointmentList.clear();

        String sql = "SELECT * FROM appointments";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                appointmentList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return appointmentList;
    }

    /** Method to clear and extract new data for current week appt list.
     **/
    public static ObservableList<Appointment> refreshCurrentWeekList(){
        viewCurrentWeekList.clear();
        String sql = "SELECT * FROM appointments WHERE WEEKOFYEAR(Start) = WEEKOFYEAR(NOW())";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                viewCurrentWeekList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return viewCurrentWeekList;
    }

    /** Method to clear and extract new data from current month appt list.
     **/
    public static ObservableList<Appointment> refreshCurrentMonthList(){
        viewCurrentMonthList.clear();
        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(NOW()) AND YEAR(Start) = YEAR(NOW())";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                viewCurrentMonthList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return viewCurrentMonthList;
    }

    /** Method to obtain the appointments for the current week.
     * */
    public static ObservableList<Appointment> viewCurrentWeekList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> getApptCurrentWeek(){
        String sql = "SELECT * FROM appointments WHERE WEEKOFYEAR(Start) = WEEKOFYEAR(NOW())";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                viewCurrentWeekList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return viewCurrentWeekList;

    }

    /** Method to extract data for appointments of the current month.
     * */
    public static ObservableList<Appointment> viewCurrentMonthList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> getApptCurrentMonth(){
        String sql = "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(NOW()) AND YEAR(Start) = YEAR(NOW())";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                viewCurrentMonthList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return viewCurrentMonthList;

    }

    /** Method to extract appointments that are within 15minutes after successful login.
     * */
    public static ObservableList<Appointment> appointmentInFifteenList = FXCollections.observableArrayList();
    public static ObservableList<Appointment> getAppointmentInFifteen(){
        String sql = "SELECT* FROM appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 15 MINUTE)";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                appointmentInFifteenList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return appointmentInFifteenList;
    }

    /** List created for holding the appointments of selected contact.
     * */
    public static ObservableList<Appointment> appointmentsPerContactList = FXCollections.observableArrayList();

    /** Method queries the appointment based on selected contact.
     * @param selectedContactId selected contact ID
     * */
    public static ObservableList<Appointment> getApptForContact(int selectedContactId){
        String sql = "SELECT * FROM appointments WHERE Contact_ID = ? ";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ps.setInt(1, selectedContactId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment A = new Appointment(appointmentId, title, description, location, type,
                        startDateTime, endDateTime, customerId, userId, contactId);
                appointmentsPerContactList.add(A);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return appointmentsPerContactList;
    }

}
