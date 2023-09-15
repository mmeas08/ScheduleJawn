package controller;

import dao.AppointmentDao;
import dao.ContactDao;
import dao.CustomerDao;
import dao.UserDao;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * The class controls the user interaction in editAppt.fxml.
 * Appt information is populated into the form. User edits the information for update in database.
 * */

public class EditApptController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public ComboBox<Customer> customerIdCB;
    public TextField typeTF;
    public TextField titleTF;
    public TextArea descriptionTF;
    public DatePicker startDateDP;
    public ComboBox<LocalTime> startTimeCB;
    public DatePicker endDateDP;
    public ComboBox<LocalTime> endTimeCB;
    public TextField locationTF;
    public ComboBox<Contact> contactCB;
    public ComboBox<User> userIdCB;
    public Label timeLabel;
    public TextField editApptIdTF;


    /** Method initializes the EditApptController.
     * Data is populated in the comboboxes and start datepicker.
     *
     *
     * @param url
     * @param resourceBundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userIdCB.setItems(UserDao.userList);
        customerIdCB.setItems(CustomerDao.customerList);
        contactCB.setItems(ContactDao.contactList);
        contactCB.setVisibleRowCount(3);
        customerIdCB.setVisibleRowCount(4);
        startTimeCB.setVisibleRowCount(6);
        endTimeCB.setVisibleRowCount(4);

        LocalTime start = LocalTime.MIDNIGHT;
        LocalTime end = LocalTime.of(22,0);

        while(start.isBefore(end)){
            startTimeCB.getItems().add(start);
            start = start.plusHours(1);
        }

        startDateDP.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.isBefore(today));
            }
        });

        ZonedDateTime zdt = ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault());
        ZonedDateTime et = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));

        DateTimeFormatter d = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss z");
        String stringZdt = zdt.format(d);
        String stringEt = et.format(d);
        timeLabel.setText("Your current datetime: " + stringZdt + "\nCurrent Eastern Time: " + stringEt);

    }

    /** Method populates the data of the selected appt from the appt table.
     *
     * @param selectedAppt
     * */
    public void displayApptInfo(Appointment selectedAppt) {

        editApptIdTF.setText(String.valueOf(selectedAppt.getAppointmentId()));

        ObservableList<Customer> cList = CustomerDao.customerList;
        for(Customer c : cList){
            if(c.getCustomerId() == selectedAppt.getCustomerId()){
                customerIdCB.setValue(c);
            }
        }
        typeTF.setText(selectedAppt.getType());
        titleTF.setText(selectedAppt.getTitle());
        descriptionTF.setText(selectedAppt.getDescription());
        startDateDP.setValue(selectedAppt.getStartDateTime().toLocalDate());
        startTimeCB.setValue(selectedAppt.getStartDateTime().toLocalTime());
        endDateDP.setValue(selectedAppt.getEndDateTime().toLocalDate());
        endTimeCB.setValue(selectedAppt.getEndDateTime().toLocalTime());
        locationTF.setText(selectedAppt.getLocation());

        ObservableList<Contact> contactList = ContactDao.contactList;
        for(Contact q : contactList){
            if(q.getContactId() == selectedAppt.getContactId()){
                contactCB.setValue(q);
            }
        }

        ObservableList<User> uList = UserDao.userList;
        for(User u : uList){
            if(u.getUserId() == selectedAppt.getUserId()){
                userIdCB.setValue(u);
            }
        }
    }


    /** User input retrieved, validated and updated in the database.
     * Validation checks for null/empty, overlaps and selection outside business hours.
     *
     * @param actionEvent add button clicked
     * */
    public void clickedAdd(ActionEvent actionEvent) {
        try{
            int apptId = Integer.parseInt(editApptIdTF.getText());
            String title = titleTF.getText();
            String description = descriptionTF.getText();
            String location = locationTF.getText();
            String type = typeTF.getText();

            LocalDate startDateLD = startDateDP.getValue();
            LocalDate endDateLD = endDateDP.getValue();

            LocalTime startTimeLT = startTimeCB.getValue();
            LocalTime endTimeLT = endTimeCB.getValue();

            Timestamp start = Timestamp.valueOf(LocalDateTime.of(startDateDP.getValue(), startTimeCB.getValue()));
            Timestamp end = Timestamp.valueOf(LocalDateTime.of(endDateDP.getValue(), endTimeCB.getValue()));
            Customer c = customerIdCB.getValue();
            int customerId = c.getCustomerId();
            User u = userIdCB.getValue();
            int userId = u.getUserId();
            Contact cn = contactCB.getValue();
            int contactId = cn.getContactId();

            if(customerIdCB == null || type.isEmpty() || title.isEmpty() || description.isEmpty() ||
                    location.isEmpty() || contactCB == null || userIdCB == null || startDateDP == null ||
                    startTimeCB == null || endDateDP == null || endTimeCB == null){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Add Appointment");
                error.setHeaderText("Input Error");
                error.setContentText("Make sure all fields are correct.");
                error.showAndWait();
            }else{
                if((AddApptController.startWithinBusinessHour(startDateLD, startTimeLT)) && (AddApptController.endWithinBusinessHour(endDateLD, endTimeLT))) {
                    if (AddApptController.noOverlappedAppt(customerId, apptId, startDateLD, startTimeLT, endTimeLT)) {
                        int rowsAffected = AppointmentDao.updateEditAppt(apptId, title, description, location, type, start, end,
                                customerId, userId, contactId);
                        AppointmentDao.refreshAppointmentList();
                        AppointmentDao.refreshCurrentMonthList();
                        AppointmentDao.refreshCurrentWeekList();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                        root = loader.load();
                        MainController msc = loader.getController();
                        msc.apptButton.fire();
                        msc.apptTable.setItems(AppointmentDao.appointmentList);
                        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                        scene = new Scene(root, 1200, 700);
                        stage.setScene(scene);
                        stage.show();
                        if (rowsAffected <= 0){
                            System.out.println("failed to update row");
                        }else {
                            System.out.println("horray! row updated");
                        }
                    }else{
                        System.out.println("overlapped jawn - clickedAdd method");
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Appointment");
                        error.setHeaderText("uh-oh.");
                        error.setContentText("Selected time overlaps with existing appointment(s). \nPlease choose different time.");
                        error.showAndWait();
                    }
                }else{
                    System.out.println("not within business hours - clickadd method");
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Business Hours");
                    error.setHeaderText("No can do - outside business hours");
                    error.setContentText("Business hours are seven days a week, 8:00 - 22:00 ET");
                    error.showAndWait();
                }
            }
        } catch(Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Edit Appointment");
            error.setHeaderText("Input Error");
            error.setContentText("Make sure all fields are correct");
            error.showAndWait();
        }
    }

    /** Method cancels the add appt screen and returns to main screen.
     *
     * @param actionEvent clicked cancel
     * */
    public void clickedCancel(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        root = loader.load();
        MainController msc = loader.getController();
        msc.apptButton.fire();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.show();
    }

    /** Method sets the end date datepicker based on the start datepicker.
     * User can only schedule appt as a day only appointment.
     * @param actionEvent a selection is made in the start date datepicker
     * */
    public void selectedStartDateDP(ActionEvent actionEvent) {
        endDateDP.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate start = startDateDP.getValue();
                endDateDP.setValue(start);
                setDisable(empty || date.isBefore(start));
            }
        });
    }

    /** Method sets the end start time combobox based on the start time combobox.
     * Time populates 1hr after the selected start hour.
     * User can only schedule appointment at 1hr increment.
     * @param actionEvent a selection made in the start time combobox
     * */
    public void selectedStartTimeCB(ActionEvent actionEvent) {
        LocalTime start2 = (startTimeCB.getSelectionModel().getSelectedItem()).plusHours(1);
        LocalTime end2 = LocalTime.of(22,0);

        endTimeCB.getItems().clear();
        System.out.println("cleared to reset end time");
        while(start2.isBefore(end2.plusHours(1))) {
            endTimeCB.getItems().add(start2);
            start2 = start2.plusHours(1);
        }
    }
}
