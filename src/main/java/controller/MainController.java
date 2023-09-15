package controller;

import Interface.DateTimeInterface;
import Interface.HideInterface;
import dao.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;



/** The class handles user interaction in main.fxml.
 * User gains access to the customer and appointment tables.
 * User can add, edit, and delete customer and appointment.
 * User can access the reports requested - appointments per contact, appointments per type and appointments per month.
 * Home is currently a work in progress.
 * */
public class MainController implements Initializable {
    public TableView<Appointment> apptTable;
    public RadioButton viewAllRB;
    public RadioButton viewMonthRB;
    public RadioButton viewWeekRB;
    public TableView<Appointment> contactApptReportTable;
    public HBox reportTableViewsHBox;
    public VBox contactVBox;
    public ComboBox<Contact> contactReportCB;
    public HBox reportTBHBox;
    public ToggleButton productivityTB;
    public ToggleGroup reportToggles;
    public ToggleButton contactTB;
    public TableColumn<Appointment, Integer> contactApptIDColumn;
    public TableColumn<Appointment, String> contactTitleColumn;
    public TableColumn<Appointment, String> contactDescriptionColumn;
    public TableColumn<Appointment, String> contactTypeColumn;
    public TableColumn<Appointment, LocalDateTime> contactStartColumn;
    public TableColumn<Appointment, LocalDateTime> contactEndColumn;
    public TableColumn<Appointment, Integer> contactCustomerIdColumn;
    public TableView<Report> countryReportTable;
    public TableView<Report> monthApptTable;
    public TableView<Report> typeApptTable;
    public TableColumn<Report, String> monthReportColumn;
    public TableColumn<Report, Integer> monthReportApptCountColumn;
    public TableColumn<Report, String> typeReportColumn;
    public TableColumn<Report, Integer> typeReportApptCountColumn;
    public TableColumn<Report, String> countryReportColumn;
    public TableColumn<Report, Integer> countryReportTotalCustomerColumn;
    public Label homeTitle;
    public Label homeLabel;
    public Label mainUserLabel;
    public Label mainDateTimeLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public ToggleButton customerButton;
    public ToggleGroup mainTGroup;
    public ToggleButton apptButton;
    public ToggleButton reportButton;
    public TableView<CustomerPlus> customerTable;
    public TableColumn<CustomerPlus, Integer> customerIdColumn;
    public TableColumn<CustomerPlus, String> customerNameColumn;
    public TableColumn<CustomerPlus, String> customerAddressColumn;
    public TableColumn<CustomerPlus, String> customerPostalCodeColumn;
    public TableColumn<CustomerPlus, String> customerDivisionColumn;
    public TableColumn<CustomerPlus, String> customerCountryColumn;
    public TableColumn<CustomerPlus, String> customerPhoneNumColumn;
    public TableColumn<Appointment, Integer> apptIdColumn;
    public TableColumn<Appointment, String> titleColumn;
    public TableColumn<Appointment, String> descripColumn;
    public TableColumn<Appointment, String>locColumn;
    public TableColumn<Appointment, String> contactColumn;
    public TableColumn<Appointment, String> typeColumn;
    public TableColumn<Appointment, LocalDateTime> startDateTimeColumn;
    public TableColumn<Appointment, LocalDateTime> endDateTimeColumn;
    public TableColumn<Appointment, Integer> apptCustomerIdColumn;
    public TableColumn<Appointment, Integer> userIdColumn;
    public HBox apptTableHBox;
    public ToggleGroup apptTableTGroup;
    public HBox addEditDeleteHBox;
    public Label customerTitle;
    public Label apptTitle;
    public Label reportTitle;

    HideInterface hideHome = ()->{
        homeTitle.setVisible(false);
        homeTitle.managedProperty().bind(homeTitle.visibleProperty());
        homeLabel.setVisible(false);
        homeLabel.managedProperty().bind(homeLabel.visibleProperty());
    };
    HideInterface hideCustomer = ()->{
        customerTable.setVisible(false);
        customerTable.managedProperty().bind(customerTable.visibleProperty());
        customerTitle.setVisible(false);
        customerTitle.managedProperty().bind(customerTitle.visibleProperty());
    };
    HideInterface hideAppt = ()->{
        apptTableHBox.setVisible(false);
        apptTableHBox.managedProperty().bind(apptTableHBox.visibleProperty());
        apptTable.setVisible(false);
        apptTable.managedProperty().bind(apptTable.visibleProperty());
        apptTitle.setVisible(false);
        apptTitle.managedProperty().bind(apptTitle.visibleProperty());
    };
    HideInterface hideCustApptAddEditDeleteHBox = ()-> {
        addEditDeleteHBox.setVisible(false);
        addEditDeleteHBox.managedProperty().bind(addEditDeleteHBox.visibleProperty());
    };
    HideInterface hideReport = ()->{
        reportTitle.setVisible(false);
        reportTitle.managedProperty().bind(reportTitle.visibleProperty());
        reportTableViewsHBox.setVisible(false);
        reportTableViewsHBox.managedProperty().bind(reportTableViewsHBox.visibleProperty());
        contactVBox.setVisible(false);
        contactVBox.managedProperty().bind(contactVBox.visibleProperty());
        reportTBHBox.setVisible(false);
        reportTBHBox.managedProperty().bind(reportTBHBox.visibleProperty());
        contactApptReportTable.setVisible(false);
        contactApptReportTable.managedProperty().bind(contactApptReportTable.visibleProperty());
    };
    HideInterface hideReportContact = ()->{
        contactVBox.setVisible(false);
        contactVBox.managedProperty().bind(contactVBox.visibleProperty());
        contactApptReportTable.setVisible(false);
        contactApptReportTable.managedProperty().bind(contactApptReportTable.visibleProperty());
    };
    HideInterface hideReportTableView = ()->{
        reportTableViewsHBox.setVisible(false);
        reportTableViewsHBox.managedProperty().bind(reportTableViewsHBox.visibleProperty());
    };


    /** Method initializes the main controller.
     * Lambda #1: The lambda here is used to format the zonedatetime of localdatetime.
     * Customer, appointment, contact, report tables are set in this method.
     * Home is selected as the start of the main screen.
     *
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clickedHome(new ActionEvent());

        DateTimeFormatter d = DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm z");
        DateTimeInterface dateTime = x -> x.format(d);
        mainDateTimeLabel.setText(dateTime.localDateTimeNow(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())));

        apptIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descripColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        apptCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        apptTable.setItems(AppointmentDao.appointmentList);


        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerPhoneNumColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTable.setItems(CustomerPlusDao.customerPlusTableList);
        customerTable.getSortOrder().add(customerNameColumn);

        contactReportCB.setItems(ContactDao.contactList);
        contactReportCB.setVisibleRowCount(6);

        contactApptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        contactTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactStartColumn.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        contactEndColumn.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        contactCustomerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));

        monthReportColumn.setCellValueFactory(new PropertyValueFactory<>("fieldName"));
        monthReportApptCountColumn.setCellValueFactory(new PropertyValueFactory<>("someValueBasedOnReport"));
        monthApptTable.setItems(ReportDao.monthReportList);

        typeReportColumn.setCellValueFactory(new PropertyValueFactory<>("fieldName"));
        typeReportApptCountColumn.setCellValueFactory(new PropertyValueFactory<>("someValueBasedOnReport"));
        typeApptTable.setItems(ReportDao.typeReportList);

        countryReportColumn.setCellValueFactory(new PropertyValueFactory<>("fieldName"));
        countryReportTotalCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("someValueBasedOnReport"));
        countryReportTable.setItems(ReportDao.countryReportList);

    }

    /** Method handles what is available to the user based on the selection of customer, appointment or report.
     * Selecting customer will show only customer stuff.
     * Selecting appointment will show only appointment stuff.
     * Selecting report will show only report stuff.
     * Lambda #2: The lambda expressions are declared outside the method due to being used in another method.
     * The lambda expressions are created to hide components on the main.fxml based on the toggle button chosen.
     * It has helped reduced the repetitiveness that existed prior to using lambda expressions.
     *
     * @param actionEvent one of the toggle buttons is selected
     * */
    public void mainToggleButton(ActionEvent actionEvent) {
        if(actionEvent.getSource() == customerButton) {
            customerTable.setVisible(true);
            customerTable.managedProperty().bind(customerTable.visibleProperty());
            customerTitle.setVisible(true);
            customerTitle.managedProperty().bind(customerTitle.visibleProperty());

            addEditDeleteHBox.setVisible(true);
            addEditDeleteHBox.managedProperty().bind(addEditDeleteHBox.visibleProperty());

            hideHome.hide();
            hideAppt.hide();
            hideReport.hide();

            customerButton.setSelected(true);
        } else if(actionEvent.getSource() == apptButton) {
            apptTableHBox.setVisible(true);
            apptTableHBox.managedProperty().bind(apptTableHBox.visibleProperty());
            apptTable.setVisible(true);
            apptTable.managedProperty().bind(apptTable.visibleProperty());
            apptTitle.setVisible(true);
            apptTitle.managedProperty().bind(apptTitle.visibleProperty());

            addEditDeleteHBox.setVisible(true);
            addEditDeleteHBox.managedProperty().bind(addEditDeleteHBox.visibleProperty());

            hideHome.hide();
            hideCustomer.hide();
            hideReport.hide();

            apptButton.setSelected(true);
        }else if(actionEvent.getSource() == reportButton){
            reportTitle.setVisible(true);
            reportTitle.managedProperty().bind(reportTitle.visibleProperty());

            hideHome.hide();
            hideCustomer.hide();
            hideAppt.hide();
            hideCustApptAddEditDeleteHBox.hide();

            reportButton.setSelected(true);
            productivityTB.fire();
        }
    }

    /** Method handles what the user will see based on the selection made in the report.
     * Productivity toggle button shows the type, month and country table reports.
     * Contact toggle button shows the contact table where user can select the contact from a combobox to see the appts for the selected contact.
     *
     *
     * @param actionEvent a toggle button is selected
     * */
    public void reportToggleAction(ActionEvent actionEvent) {
        if(actionEvent.getSource() == productivityTB){
            reportTBHBox.setVisible(true);
            reportTBHBox.managedProperty().bind(reportTBHBox.visibleProperty());

            reportTableViewsHBox.setVisible(true);
            reportTableViewsHBox.managedProperty().bind(reportTableViewsHBox.visibleProperty());

            hideReportContact.hide();

            productivityTB.setSelected(true);
        }else if(actionEvent.getSource() == contactTB){
            reportTBHBox.setVisible(true);
            reportTBHBox.managedProperty().bind(reportTBHBox.visibleProperty());

            contactVBox.setVisible(true);
            contactVBox.managedProperty().bind(contactVBox.visibleProperty());
            contactApptReportTable.setVisible(true);
            contactApptReportTable.managedProperty().bind(contactApptReportTable.visibleProperty());

            hideReportTableView.hide();

            contactTB.setSelected(true);
        }
    }

    /** Method logs user out of application.
     *
     * @param actionEvent logout button clicked
     * */
    public void clickedLogout(ActionEvent actionEvent) {

        System.out.println("User has successfully logged out");
        System.exit(0);
    }

    /** Method handles what is visible to the user when clicking on Home button.
     * Home is a work in progress.
     *
     * @param actionEvent home button clicked
     * */
    public void clickedHome(ActionEvent actionEvent) {

        try{
            if(mainTGroup != null){
                mainTGroup.getSelectedToggle().setSelected(false);
            }
        }catch(Exception ignored){
        }

        homeTitle.setVisible(true);
        homeTitle.managedProperty().bind(homeTitle.visibleProperty());
        homeLabel.setVisible(true);
        homeLabel.managedProperty().bind(homeLabel.visibleProperty());

        hideCustomer.hide();
        hideAppt.hide();
        hideCustApptAddEditDeleteHBox.hide();
        hideReport.hide();

        homeLabel.setText("Hello " + "there" + "\nHome is a work in progress\nSelect a tab on the left\nTo begin your journey =]");
    }

    
    /** Method handles which add screen pops up based on the selected toggle button.
     * Customer button selected leads to the add customer fxml.
     * Appt button selected leads to the add appt fxml.
     *
     * @param actionEvent add button clicked
     * */
    public void clickedAdd(ActionEvent actionEvent) throws IOException {
        if(customerButton.isSelected()){
            root = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root, 700, 700);
            stage.setScene(scene);
            stage.show();
        }
        if(apptButton.isSelected()){
            root = FXMLLoader.load(getClass().getResource("/view/addAppt.fxml"));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root, 730, 700);
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Method handles which edit screen is shown based on the selected toggle button - customer or appt.
     * Customer button selected leads to editCustomer fxml.
     * Appt button selected leads to editAppt fxml.
     *
     *
     * @param actionEvent edit button clicked
     * */
    public void clickedEdit(ActionEvent actionEvent) throws IOException {

        if(customerButton.isSelected()){
            if(customerTable.getSelectionModel().getSelectedItem() != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editCustomer.fxml"));
                root = loader.load();

                EditCustomerController ecc = loader.getController();
                ObservableList<Customer> cList = CustomerDao.customerList;
                for(Customer c : cList){
                    if(c.getCustomerId() == customerTable.getSelectionModel().getSelectedItem().getCustomerId()){
                        ecc.displayCustomerInfo(c);
                    }
                }

                stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root, 700, 700);
                stage.setScene(scene);
                stage.show();
            }else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Edit Error");
                error.setHeaderText("I didn't see a selection made.");
                error.setContentText("Please select a customer first.");
                error.showAndWait();
            }
        }
        if(apptButton.isSelected()){
            if(apptTable.getSelectionModel().getSelectedItem() != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/editAppt.fxml"));
                root = loader.load();

                EditApptController eac = loader.getController();
                eac.displayApptInfo(apptTable.getSelectionModel().getSelectedItem());

                stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root, 730, 700);
                stage.setScene(scene);
                stage.show();
            }else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Edit Error");
                error.setHeaderText("I didn't see a selection made.");
                error.setContentText("Please select an appointment first.");
                error.showAndWait();
            }
        }
    }

    /** Method deletes customer or appt based on the selected toggle button.
     * Customer is checked for any existing appts before delete.
     * If customer has appt, error will inform user of deleting appt before customer deletion.
     * Appt deletion is proceeded with a refresh of the view all, view month and view week data.
     * @param actionEvent delete button clicked
     * */
    public void clickedDelete(ActionEvent actionEvent) throws SQLException {

        if(customerButton.isSelected()){
            CustomerPlus selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if(selectedCustomer != null ) {
                if (AppointmentDao.doesCustomerHaveAppointment(selectedCustomer.getCustomerId())) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Delete Error");
                    error.setHeaderText("Uh-Oh.  \n    no can do.");
                    error.setContentText("Customer have associated appointment(s). \nPlease delete appointment(s) first.");
                    error.showAndWait();
                } else {
                    CustomerDao.deleteCustomer(selectedCustomer.getCustomerId());
                    CustomerDao.refreshCustomerList();
                    CustomerPlusDao.refreshCustomerPlusTableList();
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Delete Customer");
                    info.setHeaderText("poof!");
                    info.setContentText(selectedCustomer.getCustomerName() + " is deleted");
                    info.showAndWait();
                }
            }else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Delete Error");
                error.setHeaderText("Decisions. Decisions.");
                error.setContentText("Please select a customer first.");
                error.showAndWait();
            }
        }
        if(apptButton.isSelected()){
            Appointment selectedAppointment = apptTable.getSelectionModel().getSelectedItem();
            if(selectedAppointment != null){
                AppointmentDao.deleteAppointment(selectedAppointment.getAppointmentId());
                AppointmentDao.refreshAppointmentList();
                AppointmentDao.refreshCurrentMonthList();
                AppointmentDao.refreshCurrentWeekList();
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Cancel Appointment");
                info.setHeaderText("poof!");
                info.setContentText("Appointment ID: " + selectedAppointment.getAppointmentId() + "\nType: " + selectedAppointment.getType() + "\nCANCELLED");
                info.showAndWait();
            }else{
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Delete Error");
                error.setHeaderText("Selection empty... ");
                error.setContentText("Select appt. first");
                error.showAndWait();
            }
        }
    }


    /** Method handles what is being presented in the appointment table based on the radiobutton chosen.
     * View all radio button shows all appts.
     * View week radio button shows appts for that week.
     * View month radio button shows appts for that month.
     *
     * */
    public void apptDifferViewsRadioButtons(ActionEvent actionEvent) {
        if(viewAllRB.isSelected()){
            apptTable.setItems(AppointmentDao.appointmentList);
        }
        if(viewWeekRB.isSelected()){
            apptTable.setItems(AppointmentDao.viewCurrentWeekList);
        }
        if(viewMonthRB.isSelected()){
            apptTable.setItems(AppointmentDao.viewCurrentMonthList);
        }
    }

    /** Method sets the table with appointments per the selected contact.
     *
     * @param actionEvent selected contact from the contact combobox
     * */
    public void selectedContactForContactReportTable(ActionEvent actionEvent) {
        Contact selectedContact = contactReportCB.getSelectionModel().getSelectedItem();
        AppointmentDao.appointmentsPerContactList.clear();
        AppointmentDao.getApptForContact(selectedContact.getContactId());
        if(AppointmentDao.appointmentsPerContactList.isEmpty()){
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Appointment");
            info.setHeaderText("Contact: " + selectedContact.getContactName());
            info.setContentText("no appointment found");
            info.showAndWait();
        }
        contactApptReportTable.setItems(AppointmentDao.appointmentsPerContactList);
    }

}
