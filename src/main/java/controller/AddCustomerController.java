package controller;

import dao.CountryDao;
import dao.CustomerDao;
import dao.CustomerPlusDao;
import dao.FirstLevelDivisionDao;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/** The class handles the user interaction with addCustomer.fxml.
 * User input is retrieved, validated and inserted into the customer table of the database.
 *
 * */
public class AddCustomerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField customerNameTF;
    public TextField customerAddressTF;
    public TextField customerPostalCodeTF;
    public ComboBox<Country> customerCountryCB;
    public ComboBox<FirstLevelDivision> customerStateProvinceCB;
    public TextField customerPhoneTF;

    /** Method initializes the AddCustomerController.
     * Loads the country data into the country combobox.
     * @param url
     * @param resourceBundle
     *
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerCountryCB.setItems(CountryDao.countryList);
    }


    /** Method sets the first level divisions based on the selection made in the country combobox.
     *
     * @param actionEvent a selection made in the country combobox
     * */
    public void enteredCustomerCountryCB(ActionEvent actionEvent) {
        Country selectedCountry = customerCountryCB.getSelectionModel().getSelectedItem();
        ObservableList<FirstLevelDivision> fList = FirstLevelDivisionDao.filteredByCountry(selectedCountry.getCountryId());
        customerStateProvinceCB.setItems(fList);
    }

    /** User input retrieved, validated and inserted into the database.
     *
     * @param actionEvent add button clicked
     * */
    public void clickedAdd(ActionEvent actionEvent) {
        try{
            int id = getId();
            String name = customerNameTF.getText();
            String address = customerAddressTF.getText();
            String postalCode = customerPostalCodeTF.getText();
            String phone = customerPhoneTF.getText();
            LocalDateTime createDate = LocalDateTime.now();
            String createdBy = "admin";
            LocalDateTime lastUpdate = LocalDateTime.now();
            String lastUpdatedBy = "admin";
            int divisionId = (customerStateProvinceCB.getValue()).getDivisionId();


            if(name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty() || customerCountryCB == null || customerStateProvinceCB == null){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Add Customer");
                error.setHeaderText("something's empty");
                error.setContentText("Make sure all fields are filled and correct.");
                error.showAndWait();
            }else{
                int rowsAffected = CustomerDao.insertAddCustomer(id, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
                System.out.println(divisionId);
                CustomerDao.refreshCustomerList();
                CustomerPlusDao.refreshCustomerPlusTableList();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                root = loader.load();
                MainController msc = loader.getController();
                msc.customerButton.fire();
                msc.customerTable.setItems(CustomerPlusDao.customerPlusTableList);
                stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root, 1200, 700);
                stage.setScene(scene);
                stage.show();
                if (rowsAffected <= 0){
                    System.out.println("failed row insert");
                }else {
                    System.out.println("yay!");
                }
            }
        }catch(Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Add Customer");
            error.setHeaderText("Input Error");
            error.setContentText("Make sure all fields are correct.");
            error.showAndWait();
        }
    }

    /** Method returns user to the main screen.
     * @param actionEvent cancel button clicked
     * */
    public void clickedCancel(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        root = loader.load();
        MainController msc = loader.getController();
        msc.customerButton.fire();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.show();
    }


    /** Method to create a unique customer ID.
     *
     * */
    private static int getId(){
        int id = 1;
        for(Customer c : CustomerDao.customerList){
            if(c.getCustomerId() == id){
                id++;
            }
        }
        return id;
    }

}
