package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lib.DBConnection;
import model.Appointment;
import model.Cache;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller for Appointment screen
 */
public class AppointmentController implements Initializable {
  private final Cache cache;
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");

  @FXML
  private GridPane rootPane;

  @FXML
  private RadioButton allRadio;

  @FXML
  private RadioButton weekRadio;

  @FXML
  private RadioButton monthRadio;

  @FXML
  private TableView<Appointment> appointmentTable;

  @FXML
  private TableColumn<?, ?> appointmentId;

  @FXML
  private TableColumn<?, ?> title;

  @FXML
  private TableColumn<?, ?> description;

  @FXML
  private TableColumn<?, ?> location;

  @FXML
  private TableColumn<?, ?> contact;

  @FXML
  private TableColumn<?, ?> type;

  @FXML
  private TableColumn<?, ?> startTime;

  @FXML
  private TableColumn<?, ?> endTime;

  @FXML
  private TableColumn<?, ?> customerId;

  @FXML
  private Button addAppointment;

  @FXML
  private Button updateAppointment;

  @FXML
  private Button deleteAppointment;

  @FXML
  private Button reportsButton;

  @FXML
  private Button customerButton;

  @FXML
  private Button exitButton;

  @FXML
  private ToggleGroup radioToggleGroup;

  /**
   * Constructor
   * @param cache main local data
   */
  public AppointmentController(Cache cache) {
    this.cache = cache;
  }

  /**
   * Initializes screen with cached data
   * @param url screen url
   * @param resourceBundle text localization resource bundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadLabels();
    appointmentTable.setItems(cache.getAllAppointments());
    radioToggleGroup = new ToggleGroup();
    allRadio.setToggleGroup(radioToggleGroup);
    weekRadio.setToggleGroup(radioToggleGroup);
    monthRadio.setToggleGroup(radioToggleGroup);
    allRadio.setSelected(true);
    weekRadio.setSelected(false);
    monthRadio.setSelected(false);
  }

  /**
   * Loads label with correct language
   */
  private void loadLabels() {
    appointmentId.setText(rb.getString("id"));
    title.setText(rb.getString("title"));
    description.setText(rb.getString("description"));
    location.setText(rb.getString("location"));
    contact.setText(rb.getString("contact"));
    type.setText(rb.getString("type"));

    startTime.setText(rb.getString("startTime"));
    endTime.setText(rb.getString("endTime"));
    customerId.setText(rb.getString("customerId"));

    addAppointment.setText(rb.getString("add"));
    updateAppointment.setText(rb.getString("update"));
    deleteAppointment.setText(rb.getString("delete"));
    customerButton.setText(rb.getString("customers"));
    exitButton.setText(rb.getString("exit"));
  }

  /**
   * Sends user to add customer screen
   * @param event when button clicks
   * @throws IOException if fails
   */
  @FXML
  void handleAddAppointment(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/addappointment.fxml"));
    AddAppointmentController controller = new AddAppointmentController(cache);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }

  /**
   * When clicked. user is taken to customer page
   * @param event button click
   * @throws IOException if fails
   */
  @FXML
  void handleCustomer(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/customer.fxml"));
    CustomerController controller = new CustomerController(cache);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }

  /**
   * Removes selected appointment from the table
   * @param event button click
   * @throws SQLException if fails to delete
   */
  @FXML
  void handleDeleteAppointment(ActionEvent event) throws SQLException {
    if (appointmentTable.getSelectionModel().getSelectedItem() != null) {
      ObservableList<Appointment> data = FXCollections.observableArrayList();
      data = appointmentTable.getItems();
      var sql = "DELETE FROM appointments WHERE Appointment_ID = "+appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId();
      try (var ps = DBConnection.open().prepareStatement(sql)) {
        ps.executeUpdate();
      }
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle(rb.getString("appointmentDeleteTitle"));
      alert.setHeaderText(rb.getString("appointmentDeleteTitle"));
      alert.setContentText(rb.getString("appointmentDeleteTitle")+" "+appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId()+", "+appointmentTable.getSelectionModel().getSelectedItem().getType());
      alert.showAndWait();
      DBConnection.close();
      data.remove(appointmentTable.getSelectionModel().getSelectedItem());
      appointmentTable.setItems(data);
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("appointmentNotFound"));
      alert.showAndWait();
    }
  }

  /**
   * Exits program
   * @param event button click
   */
  @FXML
  void handleExit(ActionEvent event) {
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
  }

  /**
   * Loads all appointments to the table
   * @param event button click
   */
  @FXML
  void handleAllRadio(ActionEvent event) {
    appointmentTable.setItems(cache.getAllAppointments());
  }

  /**
   * loads the appointments in current month
   * @param event button click
   */
  @FXML
  void handleMonthRadio(ActionEvent event) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneMonth = now.plusMonths(1);

    FilteredList<Appointment> data = new FilteredList<>(cache.getAllAppointments());

    /**
     * Lambda function to get the appointments between the date and one month away.
     */
    data.setPredicate(row -> {
      LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
      return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(oneMonth);
    });
    appointmentTable.setItems(data);
  }

  /**
   * Loads appointment in current week
   * @param event button click
   */
  @FXML
  void handleWeekRadio(ActionEvent event) {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime oneWeek = now.plusWeeks(1);

    FilteredList<Appointment> data = new FilteredList<>(cache.getAllAppointments());

    /**
     * Lambda function to get the appointments between the date and one week away.
     */
    data.setPredicate(row -> {
      LocalDateTime rowDate = row.getLocalStart().toLocalDateTime();
      return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(oneWeek);
    });
    appointmentTable.setItems(data);
  }

  /**
   * When clicked, user is sent to reports page
   * @param event button click
   * @throws IOException if failed
   */
  @FXML
  void handleReports(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/report.fxml"));
    ReportController controller = new ReportController(cache);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }

  /**
   * Checks if appointment is selected then opens update pane
   * if no appointment selected, show alert
   * @param event button click
   */
  @FXML
  void handleUpdateAppointment(ActionEvent event) throws IOException {
    Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
    if (selectedAppointment != null) {
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/updateappointment.fxml"));
      UpdateAppointmentController controller = new UpdateAppointmentController(cache, selectedAppointment);
      loader.setController(controller);
      stage.setScene(new Scene(loader.load()));
      stage.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("selectAppointment"));
      alert.showAndWait();
    }
  }

}

