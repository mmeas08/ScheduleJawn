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
import java.sql.Date;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/** The class handles user interaction with addAppt.fxml.
 * User input data through textfield, textarea, datepicker, and combobox.
 * */
public class AddApptController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public ComboBox<Customer> addApptCustomerIdCB;
    public TextField addApptTypeTF;
    public TextField addApptTitleTF;
    public TextArea addApptDescriptionTA;
    public DatePicker addApptStartDateDP;
    public DatePicker addApptEndDateDP;
    public ComboBox<LocalTime> addApptStartTimeCB;
    public ComboBox<LocalTime> addApptEndTimeCB;
    public TextField addApptLocationTF;
    public ComboBox<Contact> addApptContactCB;
    public ComboBox<User> addApptUserIdCB;
    public Label businessHrLabel;
    public TextField addApptIdTF;


    /** The method initializes the AddApptController.
     * Data is set into the comboboxes.
     * Datepicker is set to allow users to only pick from today onwards.
     * User can only set a single day appointment.
     * Appointments are set as 1hr intervals.
     * Localdatetime and Zonedatetime (ET) available to user to check comparison when scheduling.
     *
     * @param resourceBundle
     * @param url
     *
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addApptCustomerIdCB.setItems(CustomerDao.customerList);
        addApptContactCB.setItems(ContactDao.contactList);
        addApptUserIdCB.setItems(UserDao.userList);
        addApptContactCB.setVisibleRowCount(3);
        addApptCustomerIdCB.setVisibleRowCount(4);
        addApptStartTimeCB.setVisibleRowCount(6);
        addApptEndTimeCB.setVisibleRowCount(4);

        LocalTime start = LocalTime.MIDNIGHT;
        LocalTime end = LocalTime.of(22,0);

        while(start.isBefore(end)){
            addApptStartTimeCB.getItems().add(start);
            start = start.plusHours(1);
        }

        addApptStartDateDP.setDayCellFactory(datePicker -> new DateCell(){
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
        businessHrLabel.setText("Your current datetime: " + stringZdt + "\nCurrent Eastern Time: " + stringEt);
    }

    /** User input is retrieved, logically validated and inserted into database.
     * Logical validation checks consist of no fields being null/empty, no appointment overlaps and appointment within
     * business hours - 8am-10pm ET.
     *
     * @param actionEvent Add button is clicked
     * */
    public void clickedAdd(ActionEvent actionEvent) {
        try{
            int appointmentId = getId();
            String title = addApptTitleTF.getText();
            String description = addApptDescriptionTA.getText();
            String location = addApptLocationTF.getText();
            String type = addApptTypeTF.getText();

            LocalDate startDateLD = addApptStartDateDP.getValue();
            LocalDate endDateLD = addApptEndDateDP.getValue();

            LocalTime startTimeLT = addApptStartTimeCB.getValue();
            LocalTime endTimeLT = addApptEndTimeCB.getValue();

            Timestamp start = Timestamp.valueOf(LocalDateTime.of(addApptStartDateDP.getValue(), addApptStartTimeCB.getValue()));
            Timestamp end = Timestamp.valueOf(LocalDateTime.of(addApptEndDateDP.getValue(), addApptEndTimeCB.getValue()));
            Timestamp createDate = Timestamp.valueOf(LocalDateTime.now());
            String createdBy = "admin";
            Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
            String lastUpdatedBy = "admin";
            Customer c = addApptCustomerIdCB.getValue();
            int customerId = c.getCustomerId();
            int userId = (addApptUserIdCB.getSelectionModel().getSelectedItem()).getUserId();
            Contact contact = addApptContactCB.getValue();
            int contactId = contact.getContactId();

            if(addApptCustomerIdCB == null || type.isEmpty() || title.isEmpty() || description.isEmpty() ||
                    location.isEmpty() || addApptContactCB == null || addApptUserIdCB == null || addApptStartDateDP == null ||
                    addApptStartTimeCB == null || addApptEndDateDP == null || addApptEndTimeCB == null){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Add Appointment");
                error.setHeaderText("Input Error");
                error.setContentText("Make sure all fields are correct.");
                error.showAndWait();
            }else{
                if((startWithinBusinessHour(startDateLD, startTimeLT)) && (endWithinBusinessHour(endDateLD, endTimeLT))){
                    if(noOverlappedAppt(customerId, appointmentId, startDateLD, startTimeLT, endTimeLT)){
                        int rowsAffected = AppointmentDao.insertAddAppt(appointmentId, title, description, location, type, start, end,
                                createDate, createdBy, lastUpdate, lastUpdatedBy, customerId,
                                userId, contactId);
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
                            System.out.println("failed appt row insert");
                        }else {
                            System.out.println("yay! appt row inserted");
                        }
                    }else{
                        Alert error = new Alert(Alert.AlertType.ERROR);
                        error.setTitle("Appointment");
                        error.setHeaderText("uh-oh.");
                        error.setContentText("Selected time overlaps with existing appointment(s). \nPlease choose different time.");
                        error.showAndWait();
                    }
                } else{
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Business Hours");
                    error.setHeaderText("Selected time error");
                    error.setContentText("Business hours are seven days a week, 8:00 - 22:00 ET. \nPlease select time within that frame.");
                    error.showAndWait();
                }
            }

        }catch(Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Add Appointment");
            error.setHeaderText("Input Error");
            error.setContentText("Make sure all fields are correct.");
            error.showAndWait();
        }
    }

    /** Method to obtain unique appointment ID.
     * @return id
     * */
    private static int getId(){
        int id = 1;
        for(Appointment a : AppointmentDao.appointmentList){
            if (a.getAppointmentId() == id){
                id++;
            }
        }
        return id;
    }

    /** Method determines if there is an existing appointment that would overlap the selected appointment datetime.
     * */
    public static boolean noOverlappedAppt(int customerId, int appointmentId, LocalDate selectedDateLD, LocalTime selectedStartTimeLT, LocalTime selectedEndTimeLT){
        ZonedDateTime zdtStart = ZonedDateTime.of(LocalDateTime.of(selectedDateLD, selectedStartTimeLT), ZoneId.systemDefault());
        ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.of("UTC"));
        int startHourUTC = utcStart.getHour();

        ZonedDateTime zdtEnd = ZonedDateTime.of(LocalDateTime.of(selectedDateLD, selectedEndTimeLT), ZoneId.systemDefault());
        ZonedDateTime utcEnd = zdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
        int endHourUTC = utcEnd.getHour();

        ObservableList<Appointment> aList = AppointmentDao.selectedCustomerAndAppt(customerId, Date.valueOf(selectedDateLD), startHourUTC, endHourUTC);
        if(aList.isEmpty()){
            System.out.println("List is empty. No overlap found");
            return true;
        } else {
            for(Appointment a : aList){
                if(a.getAppointmentId() == appointmentId){
                    return true;
                }
            }
            System.out.println("List has entry. Overlap found");
            return false;
        }
    }


    /** Method used to determine if selected start localdatetime is within business hours.
     * */
    public static boolean startWithinBusinessHour(LocalDate selectedDateLD, LocalTime selectedTimeLT){
        ZonedDateTime zdt = ZonedDateTime.of(selectedDateLD, selectedTimeLT, ZoneId.systemDefault());
        ZonedDateTime et = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));

        int hour = et.getHour();
        if((hour >= 8) && (hour < 22)){
            return true;
        }
        return false;
    }

    /** Method used to determine if selected end localdatetime is within business hours.
     *
     * */
    public static boolean endWithinBusinessHour(LocalDate selectedDateLD, LocalTime selectedTimeLT){
        ZonedDateTime zdt = ZonedDateTime.of(selectedDateLD, selectedTimeLT, ZoneId.systemDefault());
        ZonedDateTime et = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));

        int hour = et.getHour();
        if((hour > 8) && (hour <= 22)){
            return true;
        }
        return false;
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
    public void selectedAddApptStartDateDP(ActionEvent actionEvent) {
        addApptEndDateDP.setDayCellFactory(datePicker -> new DateCell(){
            public void updateItem(LocalDate date, boolean empty){
                super.updateItem(date, empty);
                LocalDate start = addApptStartDateDP.getValue();
                addApptEndDateDP.setValue(start);
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
        LocalTime start2 = (addApptStartTimeCB.getSelectionModel().getSelectedItem()).plusHours(1);
        LocalTime end2 = LocalTime.of(22,0);

        addApptEndTimeCB.getItems().clear();
        System.out.println("cleared to reset end time");
        while(start2.isBefore(end2.plusHours(1))) {
            addApptEndTimeCB.getItems().add(start2);
            start2 = start2.plusHours(1);
        }

    }
}
