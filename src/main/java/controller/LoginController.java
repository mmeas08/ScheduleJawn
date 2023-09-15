package controller;

import dao.AppointmentDao;
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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.*;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/** The class implements the user interaction in the login.fxml.
 *
 * */
public class LoginController implements Initializable {
    public Label LDTLabel;
    public Label loginTitleLabel;
    public Label timezoneLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public PasswordField passwordTF;
    public TextField usernameTF;

    /** Method initializes the login controller.
     * The login screen is set to translate to French if default computer setting is French.
     * User timezone is specified on the login screen.
     *
     *
     * @param url
     * @param resourceBundle
     *
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
            loginTitleLabel.setText(rb.getString("login"));
            usernameTF.setPromptText(rb.getString("username"));
            passwordTF.setPromptText(rb.getString("password"));
            timezoneLabel.setText(rb.getString("Timezone"));
        }catch(MissingResourceException e){
            System.out.println(e);
        }

        LDTLabel.setText(String.valueOf(ZoneId.systemDefault()));

    }

    /** Method validates the username and password input for access.
     * Login screen error messages are set to translate to French based on default computer setting.
     * User login attempts are recorded in the login_activity.txt.
     * User is informed if there is an appointment within 15mins or not after a successful login.
     *
     * @param actionEvent pressed enter/return in the password textfield
     */
    public void enterPasswordTF(ActionEvent actionEvent) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());

        String filename = "login_activity.txt";
        FileWriter fw = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fw);

        String enteredUsername = usernameTF.getText();
        String enteredPassword = passwordTF.getText();
        if(enteredUsername.isEmpty() || enteredPassword.isEmpty()){
            Alert missing = new Alert(Alert.AlertType.ERROR);
            missing.setTitle(rb.getString("login"));
            missing.setHeaderText(rb.getString("error"));
            missing.setContentText(rb.getString("missingerror"));
            missing.getDialogPane().getButtonTypes().clear();
            missing.getDialogPane().getButtonTypes().add(new ButtonType(rb.getString("Ok"), ButtonBar.ButtonData.OK_DONE));
            missing.showAndWait();
        }else{
            if(UserDao.userAccessOrNaw(enteredUsername, enteredPassword)){
                outputFile.println("Username: " + enteredUsername + " successfully logged in at " + (Timestamp.valueOf(LocalDateTime.now())));
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                root = loader.load();
                stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root, 1200, 700);
                stage.setScene(scene);
                stage.show();

                if(AppointmentDao.getAppointmentInFifteen().isEmpty()){
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Appointment");
                    info.setHeaderText("NO RUSH");
                    info.setContentText("You have no appointment within 15 minutes");
                    info.showAndWait();
                }else{
                    ObservableList<Appointment> aList = AppointmentDao.appointmentInFifteenList;
                    int id = -1;
                    LocalDate apptSDate = null;
                    LocalTime apptSTime = null;
                    LocalTime apptETime = null;
                    for(Appointment a : aList){
                        id = a.getAppointmentId();
                        apptSDate = LocalDate.from(a.getStartDateTime());
                        apptSTime = LocalTime.from(a.getStartDateTime());
                        apptETime = LocalTime.from(a.getEndDateTime());
                    }
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Appointment Reminder");
                    info.setHeaderText("Appointment Soon");
                    info.setContentText("You have an appointment within 15min.\nApptID: " + id + "\nDate: " + apptSDate + "\nTime: " + apptSTime + " - " + apptETime);
                    info.showAndWait();
                }
            }else{
                outputFile.println("Username: " + enteredUsername + " failed logging in at " + (Timestamp.valueOf(LocalDateTime.now())));
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle(rb.getString("login"));
                error.setHeaderText(rb.getString("error"));
                error.setContentText(rb.getString("invaliderror"));
                error.getDialogPane().getButtonTypes().clear();
                error.getDialogPane().getButtonTypes().add(new ButtonType(rb.getString("Ok"), ButtonBar.ButtonData.OK_DONE));
                error.showAndWait();
            }
        }
        outputFile.close();
        System.out.println("File written");
    }
}
