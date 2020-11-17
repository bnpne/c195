package controllers;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lib.DBConnection;
import model.Appointment;
import model.Cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for Login Pane.
 * @author Ben Paine
 */
public class LoginController implements Initializable {

  private final Cache cache;
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");

  @FXML
  private TextField usernameInput;

  @FXML
  private Label usernameLabel;

  @FXML
  private Label passwordLabel;

  @FXML
  private Label locationLabel;

  @FXML
  private Button loginButton;

  @FXML
  private Button exitButton;

  @FXML
  private PasswordField passwordInput;

  /**
   * Initializes pane with correct language
   * @param url the correct fxml
   * @param resourceBundle language
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    locationLabel.setText(this.cache.getLocale().getDisplayCountry());

    loginButton.setText(rb.getString("login"));
    exitButton.setText(rb.getString("exit"));
    usernameLabel.setText(rb.getString("username"));
    usernameInput.setPromptText(rb.getString("username"));
    passwordLabel.setText(rb.getString("password"));
    passwordInput.setPromptText(rb.getString("password"));
  }

  /**
   * Constructor that sets correct cache
   * @param cache local data
   */
  public LoginController(Cache cache) {
    this.cache = cache;
  }

  /**
   * Closes the window
   * @param event button click
   */
  @FXML
  void handleExit(ActionEvent event) {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }

  /**
   * validates the user input and logs user in. Logs the activity in login_activity.txt
   * @param event
   */
  @FXML
  void handleLogin(ActionEvent event) throws IOException {
    boolean valid = false;
    String username = usernameInput.getText();
    String password = passwordInput.getText();
    String loginActivity = "";
    Timestamp time = new Timestamp(new Date(System.currentTimeMillis()).getTime());

    try {
      String sql = "SELECT * FROM users WHERE User_Name = '" + username + "';";
      Statement stmt = DBConnection.open().createStatement();
      ResultSet result = stmt.executeQuery(sql);

      while (result.next()) {
        String user = result.getString("User_Name");
        String pass = result.getString("Password");

        if (user.matches(username) && password.matches(pass)) {
          valid = true;
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    if (valid) {
      appointmentAlert();
      loginActivity = time + ": success by " + username + "\n";
      cache.addUser(username);

      Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/appointment.fxml"));
      controllers.AppointmentController controller = new controllers.AppointmentController(cache);
      loader.setController(controller);
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    }
    else {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle(rb.getString("invalidLoginTitle"));
      alert.setHeaderText(rb.getString("invalidLoginMessage"));
      alert.showAndWait();

      loginActivity = time+": fail by " + username + "\n";
    }

    /**
     * Print to login_activity.txt
     */
    PrintWriter pw = new PrintWriter(new FileOutputStream(
            new File("login_activity.txt"), true));
    pw.append(loginActivity);
    pw.close();

  }

  /**
   * Alerts user when appointment is in 15 minutes
   * Lambda function to help filter results between now and 15 minutes
   * returns results within 15 minutes
   */
  private void appointmentAlert() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime inFifteen = now.plusMinutes(15);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd KK:mm:ss a");
    FilteredList<Appointment> filteredData = new FilteredList<>(cache.getAllAppointments());

    /**
     * Lambda function to help filter results between now and 15 minutes
     */
    filteredData.setPredicate(row -> {
      LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
      return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(inFifteen);
    });
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    if (!filteredData.isEmpty()) {
      StringBuilder appointment_message = new StringBuilder(rb.getString("appointmentAlertMessage") + "\n");
      for (Appointment filteredDatum : filteredData) {
        appointment_message.append(rb.getString("appointment")).append(" ").append(filteredDatum.getAppointmentId()).append(": ").append(filteredDatum.getLocalStart().toLocalDateTime().format(formatter)).append(" \n");
      }
      alert.setTitle(rb.getString("appointmentAlertTitle"));
      alert.setHeaderText(rb.getString("appointmentAlertTitle"));
      alert.setContentText(appointment_message.toString());
    } else {
      alert.setTitle(rb.getString("noAppointmentTitle"));
      alert.setHeaderText(rb.getString("noAppointmentTitle"));
      alert.setContentText(rb.getString("noAppointmentMessage")+ "\n");
    }

    alert.showAndWait();
  }

}

