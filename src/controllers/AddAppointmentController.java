package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import lib.Time;
import model.Cache;
import lib.AppointmentDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Adds appointment
 * @author Ben Paine
 */
public class AddAppointmentController implements Initializable {

  private final Cache cache;
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
  private List<String> customers = new ArrayList<>();
  private List<String> contacts = new ArrayList<>();
  private List<String> users = new ArrayList<>();

  @FXML
  private GridPane rootPane;

  @FXML
  private Label appointmentIdLabel;

  @FXML
  private TextField appointmentIdField;

  @FXML
  private Label titleLabel;

  @FXML
  private TextField titleInput;

  @FXML
  private Label descriptionLabel;

  @FXML
  private TextField descriptionInput;

  @FXML
  private Label locationLabel;

  @FXML
  private TextField locationInput;

  @FXML
  private Label contactLabel;

  @FXML
  private ComboBox<String> contactBoxInput;

  @FXML
  private Label typeLabel;

  @FXML
  private TextField typeInput;

  @FXML
  private Label startLabel;

  @FXML
  private DatePicker startTimeInput;

  @FXML
  private Label startTimeLabel;

  @FXML
  private ComboBox<String> startHour;

  @FXML
  private ComboBox<String> startMin;

  @FXML
  private Label endLabel;

  @FXML
  private DatePicker endTimeInput;

  @FXML
  private Label endTimeLabel;

  @FXML
  private ComboBox<String> endHour;

  @FXML
  private ComboBox<String> endMin;

  @FXML
  private Label customerIdLabel;

  @FXML
  private ComboBox<String> customerBoxInput;

  @FXML
  private Label userLabel;

  @FXML
  private ComboBox<String> userBoxInput;

  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

  /**
   * constructor
   * @param cache the main local data
   */
  public AddAppointmentController(Cache cache) {
    this.cache = cache;
  }

  /**
   * SCreen with labels for correct language
   * @param url screen url
   * @param resourceBundle text localization resource bundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadLabels();
    loadComboBoxes();
    loadTime();
  }

  /**
   * Loads correct time
   */
  private void loadTime() {
    ObservableList<String> startHours = FXCollections.observableArrayList();
    ObservableList<String> endHours = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ZoneId estZone = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startTime = LocalDateTime.parse("2020-10-20 08:00", formatter);
    LocalDateTime endTime = LocalDateTime.parse("2020-10-20 22:00", formatter);
    ZonedDateTime EstStart = ZonedDateTime.of(startTime, estZone);
    ZonedDateTime EstEnd = ZonedDateTime.of(endTime, estZone);
    ZonedDateTime localStart = EstStart.withZoneSameInstant(ZoneId.systemDefault());
    ZonedDateTime localEnd = EstEnd.withZoneSameInstant(ZoneId.systemDefault());

    for (int i= localStart.getHour(); i <= localEnd.getHour(); i++) {
      if (i<10) {
        startHours.add("0"+ i);
        endHours.add("0"+ i);

      } else {
        endHours.add(Integer.toString(i));
        if (i < localEnd.getHour() && localEnd.getMinute() == 0) {
          startHours.add(Integer.toString(i));
        }
      }
    }

    startMinutes.addAll("00", "15", "30", "45");
    endMinutes.addAll("00", "15", "30", "45");
    startHour.setItems(startHours);
    startMin.setItems(startMinutes);
    endHour.setItems(endHours);
    endMin.setItems(endMinutes);
  }

  /**
   * Loads dropdowns
   */
  private void loadComboBoxes() {
    this.contactBoxInput.getItems().clear();
    this.contacts.clear();
    this.customerBoxInput.getItems().clear();
    this.customers.clear();
    this.userBoxInput.getItems().clear();
    this.users.clear();
    for (int i = 0; i < cache.getAllContacts().size(); i++) {
      String contact = cache.getAllContacts().get(i).getName();
      contactBoxInput.getItems().add(contact);
      this.contacts.add(i, contact);
    }
    for (int i = 0; i < cache.getAllCustomers().size(); i++) {
      String customer = cache.getAllCustomers().get(i).getCustomerName();
      this.customerBoxInput.getItems().add(customer);
      this.customers.add(customer);
    }
    try {
      Statement stmtOne = DBConnection.open().createStatement();
      String sql = "SELECT * FROM users";
      ResultSet result = stmtOne.executeQuery(sql);
      while (result.next()) {
        String user = result.getString("User_Name");
        this.userBoxInput.getItems().add(user);
        this.users.add(user);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads labels with correct language
   */
  private void loadLabels() {
    titleLabel.setText(rb.getString("addAppointment"));
    appointmentIdLabel.setText(rb.getString("id"));
    appointmentIdField.setPromptText(rb.getString("disabledAutoGen"));
    titleLabel.setText(rb.getString("addCustomer"));
    titleLabel.setText(rb.getString("title"));
    titleInput.setPromptText(rb.getString("title"));
    descriptionLabel.setText(rb.getString("description"));
    descriptionInput.setPromptText(rb.getString("description"));
    locationLabel.setText(rb.getString("location"));
    locationInput.setPromptText(rb.getString("location"));
    contactLabel.setText(rb.getString("contact"));
    typeLabel.setText(rb.getString("type"));
    typeInput.setPromptText(rb.getString("type"));
    startLabel.setText(rb.getString("startTime"));
    endLabel.setText(rb.getString("endTime"));
    customerIdLabel.setText(rb.getString("customerId"));
    saveButton.setText(rb.getString("save"));
    cancelButton.setText(rb.getString("cancel"));
  }

  /**
   * Cancels and sends user back to the main appointment screen
   * @param event cancel button
   */
  @FXML
  void handleCancel(ActionEvent event) throws IOException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(rb.getString("cancelConfirmation"));
    alert.setContentText(rb.getString("cancelAppointmentMessage"));
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
      Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/appointment.fxml"));
      controllers.AppointmentController controller = new controllers.AppointmentController(cache);
      loader.setController(controller);
      stage.setScene(new Scene(loader.load()));
      stage.show();
    }
  }

  /**
   * Validates appointment
   * @param utcStart start time
   * @param utcEnd end time
   * @param customerId customer ID
   * @param appointmentId appointment ID
   * @return true or false
   * @throws SQLException if fails
   */
  private boolean validateAppointment(Timestamp utcStart, Timestamp utcEnd, int customerId, int appointmentId) throws SQLException {
    ZonedDateTime UtcStart = ZonedDateTime.of(utcStart.toLocalDateTime(), ZoneId.of("UTC"));
    ZonedDateTime EstStart = UtcStart.withZoneSameInstant(ZoneId.of("America/New_York"));
    ZonedDateTime UtcEnd = ZonedDateTime.of(utcEnd.toLocalDateTime(), ZoneId.of("UTC"));
    ZonedDateTime EstEnd = UtcEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

    if (EstStart.getHour() > 22 || EstStart.getHour() < 8 || EstEnd.getHour() > 22 || EstEnd.getHour() < 8) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("businessHours"));
      alert.setHeaderText(rb.getString("businessHoursMessage"));
      alert.showAndWait();
      return false;
    }

    /**
     * Calls dao and validates
     */
    if (!AppointmentDAO.validate(utcStart, utcEnd, customerId, appointmentId)) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("scheduleConflict"));
      alert.setHeaderText(rb.getString("customerScheduleConflict"));
      alert.showAndWait();
      return false;
    }
    return true;
  }

  /**
   * Increments and creates a new id. so the database doesnt have to be sorted
   * @return id
   * @throws SQLException if fails
   */
  public static int getNewId() throws SQLException {
    String sql = "SELECT * FROM appointments";
    Statement stmt = DBConnection.open().createStatement();
    ResultSet result = stmt.executeQuery(sql);
    int count = 0;
    while (result.next()) {
      if (result.getInt("Appointment_ID") > count) count = result.getInt("Appointment_ID");
    }
    DBConnection.close();
    return count + 1;
  }

  @FXML
  void handleSave(ActionEvent event) throws SQLException, IOException {
    int newId = 0;
    boolean valid = false;
    newId = getNewId();
    String titleFieldText = titleInput.getText();
    String descriptionFieldText = descriptionInput.getText();
    String locationFieldText = locationInput.getText();
    String contactFieldText = contactBoxInput.getSelectionModel().getSelectedItem();
    String typeFieldText = typeInput.getText();
    LocalDate startTime = startTimeInput.getValue();
    String startHourValue = startHour.getValue();
    String startMinValue = startMin.getValue();
    LocalDate endTime = endTimeInput.getValue();
    String endHourValue = endHour.getValue();
    String endMinValue = endMin.getValue();

    if (titleFieldText.equals("") || descriptionFieldText.equals("") || locationFieldText.equals("") || contactFieldText.equals("")
            || typeFieldText.equals("") || startTime == null || startHourValue.equals("") || startMinValue.equals("") || endTime == null ||
            endHourValue.equals("") || endMinValue.equals("")) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("blank"));
      alert.showAndWait();
    }
    else {
      //TODO: FIX THIS TIME CONVERSION
      Time localTime = new Time();
      Timestamp utcStartTime = localTime.convertTimeToUTC(startTime, startHourValue, startMinValue);
      Timestamp utcEndTime = localTime.convertTimeToUTC(endTime, endHourValue, endMinValue);
      Timestamp localStartTimestamp = localTime.convertTimeToLocal(startTime, startHourValue, startMinValue);
      Timestamp localEndTimestamp = localTime.convertTimeToLocal(endTime, endHourValue, endMinValue);

      Timestamp current_date = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
      String created_by = cache.getUser();
      String last_updated_by = cache.getUser();
      String customerIdFieldText = customerBoxInput.getSelectionModel().getSelectedItem();
      String userBoxFieldText = userBoxInput.getSelectionModel().getSelectedItem();
      String sql = "SELECT * FROM contacts WHERE Contact_Name = '"+contactFieldText+"';";
      Statement stmtOne = DBConnection.open().createStatement();
      ResultSet resultOne = stmtOne.executeQuery(sql);
      int contactId = 0;
      while (resultOne.next()) {
        contactId = resultOne.getInt("Contact_ID");
      }


      String sqlTwo = "SELECT * FROM customers WHERE Customer_Name = '"+customerIdFieldText+"';";
      stmtOne = DBConnection.open().createStatement();
      ResultSet resultTwo = stmtOne.executeQuery(sqlTwo);
      int customerId = 0;
      while (resultTwo.next()) {
        customerId = resultTwo.getInt("Customer_ID");
      }


      String sqlThree = "SELECT * FROM users WHERE User_Name = '"+userBoxFieldText+"';";
      stmtOne = DBConnection.open().createStatement();
      ResultSet resultThree = stmtOne.executeQuery(sqlThree);
      int userId = 0;
      while (resultThree.next()) {
        userId = resultThree.getInt("User_ID");
      }

      if (validateAppointment(utcStartTime, utcEndTime, customerId, newId)) {
        cache.addAppointment(AppointmentDAO.createAppointment(newId, titleFieldText, descriptionFieldText, locationFieldText, typeFieldText, localEndTimestamp, localStartTimestamp, current_date, contactId, utcStartTime, utcEndTime, "created_by", "last_updated_by", customerId, userId));
        valid=true;
      }
      if (valid) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/appointment.fxml"));
        controllers.AppointmentController controller = new controllers.AppointmentController(cache);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
      } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("error"));
        alert.setHeaderText(rb.getString("error"));
        alert.showAndWait();
      }
    }
  }

  /**
   * Updates dropdowns
   * @param event on click
   */
  @FXML
  void updateDropdown(ActionEvent event) {
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    endMin.getItems().clear();
    String endHourValue = endHour.getValue();
    ZoneId estZone = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startTime = LocalDateTime.parse("2020-10-20 08:00", formatter);
    LocalDateTime endTime = LocalDateTime.parse("2020-10-20 22:00", formatter);
    ZonedDateTime EstStart = ZonedDateTime.of(startTime, estZone);
    ZonedDateTime EstEnd = ZonedDateTime.of(endTime, estZone);
    ZonedDateTime localStart = EstStart.withZoneSameInstant(ZoneId.systemDefault());
    ZonedDateTime localEnd = EstEnd.withZoneSameInstant(ZoneId.systemDefault());
    if (Integer.parseInt(endHourValue) == localEnd.getHour() && localEnd.getMinute() == 0) {
      endMinutes.addAll("00");
    } else if (Integer.parseInt(endHourValue) == localStart.getHour()  && localStart.getMinute() == 0) {
      endMinutes.addAll("15", "30", "45");
    }
    else {
      endMinutes.addAll("00", "15", "30", "45");
    }
    startMinutes.addAll("00", "15", "30", "45");
    endMin.setItems(endMinutes);
    startMin.setItems(startMinutes);
  }

}
