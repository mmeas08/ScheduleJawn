package dao;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Class for queries of the contact table in the database.
 * */
public class ContactDao {

    /**
     * List created to hold the contacts from the database contact table.
     * */
    public static ObservableList<Contact> contactList = FXCollections.observableArrayList();

    /**
     * Method queries the contact table for contact list.
     * */
    public static ObservableList<Contact> getAllContact(){
        String sql = "SELECT * FROM contacts";
        try{
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                Contact contact = new Contact(contactId, contactName, contactEmail);
                contactList.add(contact);
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return contactList;
    }
}
