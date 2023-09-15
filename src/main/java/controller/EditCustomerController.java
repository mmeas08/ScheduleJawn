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
import java.util.ResourceBundle;

/** The class handles the user interaction with editCustomer.fxml.
 * User input is retrieved, validated and updated in the customer table of the database.
 *
 * */
public class EditCustomerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField customerIdTF;
    public TextField editCustomerNameTF;
    public TextField editCustomerAddressTF;
    public TextField editPostalCodeTF;
    public ComboBox<Country> editCustomerCountryCB;
    public ComboBox<FirstLevelDivision> editCustomerStateProvinceCB;
    public TextField editCustomerPhoneTF;

    /** Method initializes the editCustomerController.
     * Loads the country data into the country combobox.
     * @param url
     * @param resourceBundle
     *
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        editCustomerCountryCB.setItems(CountryDao.countryList);
    }

    /** Method populates data for the selected customer from the customer table.
     *
     * @param selectedCustomer customer selected from the main screen - customer table
     * */
    public void displayCustomerInfo(Customer selectedCustomer){

        customerIdTF.setText(String.valueOf(selectedCustomer.getCustomerId()));
        editCustomerNameTF.setText(selectedCustomer.getCustomerName());
        editCustomerAddressTF.setText(selectedCustomer.getCustomerAddress());
        editPostalCodeTF.setText(selectedCustomer.getPostalCode());
        editCustomerPhoneTF.setText(selectedCustomer.getPhone());

        ObservableList<FirstLevelDivision> fList = FirstLevelDivisionDao.firstLevelDivisionList;
        for(FirstLevelDivision z : fList){
            if(z.getDivisionId() == selectedCustomer.getDivisionId()){
                editCustomerStateProvinceCB.setValue(z);
                ObservableList<Country> cList = CountryDao.countryList;
                for(Country c : cList){
                    if(c.getCountryId() == z.getCountryId()){
                        editCustomerCountryCB.setValue(c);
                        break;
                    }
                }
            }
        }
    }

    /** Method sets the first level divisions based on the selection made in the country combobox.
     *
     * @param actionEvent a selection made in the country combobox
     * */
    public void enteredEditCustomerCountryCB(ActionEvent actionEvent) {
        Country selectedCountry = editCustomerCountryCB.getValue();
        ObservableList<FirstLevelDivision> fList = FirstLevelDivisionDao.filteredByCountry(selectedCountry.getCountryId());
        editCustomerStateProvinceCB.setItems(fList);
    }

    /** User input retrieved, validated and inserted into the database.
     *
     * @param actionEvent add button clicked
     * */
    public void clickedAdd(ActionEvent actionEvent) {
        try{
            int id = Integer.parseInt(customerIdTF.getText());
            String name = editCustomerNameTF.getText();
            String address = editCustomerAddressTF.getText();
            String postalCode = editPostalCodeTF.getText();
            String phone = editCustomerPhoneTF.getText();
            int divisionId = editCustomerStateProvinceCB.getValue().getDivisionId();

            if(name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || phone.isEmpty() || editCustomerCountryCB == null || editCustomerStateProvinceCB == null){
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Edit Customer");
                error.setHeaderText("something's empty");
                error.setContentText("Make sure all fields are filled and correct");
                error.showAndWait();
            }else{
                int rowsAffected = CustomerDao.updateEditCustomer(id, name, address, postalCode, phone, divisionId);
                System.out.println("ID: " + id + " name: " + name);
                System.out.println(address);
                System.out.println(postalCode);
                System.out.println(phone);
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
                    System.out.println("failed to update row");
                }else {
                    System.out.println("horray! row updated");
                }
            }
        }catch(Exception e){
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Edit Customer");
            error.setHeaderText("Input Error");
            error.setContentText("Make sure all fields are correct");
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

}
