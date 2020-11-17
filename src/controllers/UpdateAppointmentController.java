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
import model.Appointment;
import model.Cache;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 * Controller for Updating an appointment
 */
public class UpdateAppointmentController implements Initializable {

  private final Cache cache;
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
  private final Appointment appointment;
  private String customer;
  private String contact;
  private String user;

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
   * Initializes update pane
   * @param cache main local data
   * @param appointment appointment to update
   */
  public UpdateAppointmentController(Cache cache, Appointment appointment) {
    this.cache = cache;
    this.appointment = appointment;
  }

  /**
   * Initializes with correct labels
   * @param url fxml file
   * @param resourceBundle language
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadLabels();
    loadComboBoxes();
    loadTime();
    setData();
  }

  /**
   * Sets input fields
   */
  private void setData() {
    appointmentIdField.setText(Integer.toString(appointment.getAppointmentId()));
    titleInput.setText(appointment.getTitle());
    descriptionInput.setText(appointment.getDescription());
    locationInput.setText(appointment.getLocation());
    customerBoxInput.setValue(this.customer);
    typeInput.setText(appointment.getType());
    startTimeInput.setValue(appointment.getLocalStart().toLocalDateTime().toLocalDate());
    startHour.setValue(Integer.toString(appointment.getLocalStart().toLocalDateTime().getHour()));
    startMin.setValue(Integer.toString(appointment.getLocalStart().toLocalDateTime().getMinute()));
    endTimeInput.setValue(appointment.getLocalEnd().toLocalDateTime().toLocalDate());
    endHour.setValue(Integer.toString(appointment.getLocalEnd().toLocalDateTime().getHour()));
    endMin.setValue(Integer.toString(appointment.getLocalEnd().toLocalDateTime().getMinute()));
    userBoxInput.setValue(this.user);
    contactBoxInput.setValue(this.contact);
  }

  /**
   * Loads time in correct time zone
   */
  private void loadTime() {
    ObservableList<String> startHours = FXCollections.observableArrayList();
    ObservableList<String> endHours = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ZoneId estZone = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startTime = LocalDateTime.parse("2020-10-31 08:00", formatter);
    LocalDateTime endTime = LocalDateTime.parse("2020-10-31 22:00", formatter);
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
   * Loads drop down boxes
   */
  private void loadComboBoxes() {
    String sqlOne = "SELECT * FROM contacts";
    String sqlTwo = "SELECT * FROM customers";
    String sqlThree = "SELECT * FROM users";
    try {
      Statement stmtOne = DBConnection.open().createStatement();
      Statement stmtTwo = DBConnection.open().createStatement();
      Statement stmtThree = DBConnection.open().createStatement();
      ResultSet resultOne = stmtOne.executeQuery(sqlOne);
      ResultSet resultTwo = stmtTwo.executeQuery(sqlTwo);
      ResultSet resultThree = stmtThree.executeQuery(sqlThree);

      this.contactBoxInput.getItems().clear();
      this.customerBoxInput.getItems().clear();
      this.userBoxInput.getItems().clear();
      while (resultOne.next()) {
        String contact = resultOne.getString("Contact_Name");
        int contactId = resultOne.getInt("Contact_ID");
        contactBoxInput.getItems().add(contact);
        if (contactId == appointment.getContactId()) this.contact = contact;
      }
      while (resultTwo.next()) {
        String customer = resultTwo.getString("Customer_Name");
        int customerId = resultTwo.getInt("Customer_ID");
        this.customerBoxInput.getItems().add(customer);
        if (customerId == appointment.getCustomerId()) this.customer = customer;
      }
      while (resultThree.next()) {
        String user = resultThree.getString("User_Name");
        int userId = resultThree.getInt("User_ID");
        this.userBoxInput.getItems().add(user);
        if (userId == appointment.getUserId()) this.user = user;
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * loads labels with correct language
   */
  private void loadLabels() {
    titleLabel.setText(rb.getString("updateAppointment"));
    appointmentIdLabel.setText(rb.getString("id"));
    appointmentIdField.setPromptText(rb.getString("disabledAutoGen"));
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
   * Confirms with user to cancel then sends user back to appointment window
   * @param event button
   * @throws IOException if fails
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
      AppointmentController controller = new AppointmentController(cache);
      loader.setController(controller);
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    }
  }

  /**
   * Validates then saves updated appointment
   * @param event on click
   */
  @FXML
  void handleSave(ActionEvent event) throws IOException {
    boolean valid = false;
    int newId = Integer.parseInt(appointmentIdField.getText());
    String titleFieldText = titleInput.getText();
    String descriptionFieldText = descriptionInput.getText();
    String locationFieldText = locationInput.getText();
    String typeFieldText = typeInput.getText();
    LocalDate startTime = startTimeInput.getValue();
    String startHourValue = startHour.getValue();
    String startMinValue = startMin.getValue();
    LocalDate endTime = endTimeInput.getValue();
    String endHourValue = endHour.getValue();
    String endMinValue = endMin.getValue();
    Timestamp current_time = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
    String last_updated_by = cache.getUser();
    String contactFieldText = contactBoxInput.getSelectionModel().getSelectedItem();
    String customerIdFieldText = customerBoxInput.getSelectionModel().getSelectedItem();
    String userBoxFieldText = userBoxInput.getSelectionModel().getSelectedItem();

    if (titleFieldText.equals("") || descriptionFieldText.equals("") || locationFieldText.equals("") || contactFieldText.equals("")
            || typeFieldText.equals("") || startTime == null || startHourValue.equals("") || startMinValue.equals("") || endTime == null ||
            endHourValue.equals("") || endMinValue.equals("")) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("blank"));
      alert.showAndWait();
    }
    else {
      Time localTime = new Time();
      Timestamp utcStartTime = localTime.convertTimeToUTC(startTime, startHourValue, startMinValue);
      Timestamp utcEndTime = localTime.convertTimeToUTC(endTime, endHourValue, endMinValue);

      Timestamp localStartTimestamp = localTime.convertTimeToLocal(startTime, startHourValue, startMinValue);
      Timestamp localEndTimestamp = localTime.convertTimeToLocal(endTime, endHourValue, endMinValue);

      try {
        String sqlOne = "SELECT * FROM contacts WHERE Contact_Name = '" + contactFieldText + "';";
        Statement stmtOne = DBConnection.open().createStatement();
        ResultSet resultOne = stmtOne.executeQuery(sqlOne);
        int contactId = 0;
        while (resultOne.next()) {
          contactId = resultOne.getInt("Contact_ID");
        }
        String sqlTwo = "SELECT * FROM customers WHERE Customer_Name = '" + customerIdFieldText + "';";
        stmtOne = DBConnection.open().createStatement();
        ResultSet resultTwo = stmtOne.executeQuery(sqlTwo);
        int customerId = 0;
        while (resultTwo.next()) {
          customerId = resultTwo.getInt("Customer_ID");
        }
        String sqlThree = "SELECT * FROM users WHERE User_Name = '" + userBoxFieldText + "';";
        stmtOne = DBConnection.open().createStatement();
        ResultSet resultThree = stmtOne.executeQuery(sqlThree);
        int userId = 0;
        while (resultThree.next()) {
          userId = resultThree.getInt("User_ID");
        }
        if (validateAppointment(utcStartTime, utcEndTime, customerId, newId)) {
          String updateSql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Last_Update = ?, Last_Updated_By = ?, Start = ?, End = ?, Contact_ID = ?, Customer_ID = ?, User_ID = ? WHERE Appointment_ID = ?";
          PreparedStatement insert = DBConnection.open().prepareStatement(updateSql);
          insert.setString(1, titleFieldText);
          insert.setString(2, descriptionFieldText);
          insert.setString(3, locationFieldText);
          insert.setString(4, typeFieldText);
          insert.setTimestamp(5, current_time);
          insert.setString(6, last_updated_by);
          insert.setTimestamp(7, localStartTimestamp);
          insert.setTimestamp(8, localEndTimestamp);
          insert.setInt(9, contactId);
          insert.setInt(10, customerId);
          insert.setInt(11, userId);
          insert.setInt(12, newId);
          appointment.setName(titleFieldText);
          appointment.setDescription(descriptionFieldText);
          appointment.setLocation(locationFieldText);
          appointment.setContactId(contactId);
          appointment.setType(typeFieldText);
          appointment.setCustomerId(customerId);
          appointment.setUserId(userId);
          appointment.setStart(utcStartTime);
          appointment.setLocalStart(localStartTimestamp);
          appointment.setFormattedLocalStart(localStartTimestamp);
          appointment.setEnd(utcEndTime);
          appointment.setLocalEnd(localEndTimestamp);
          appointment.setFormattedLocalEnd(localEndTimestamp);
          appointment.setLastUpdate(current_time);
          appointment.setLastUpdatedBy(last_updated_by);
          try (var ps = insert) {
            ps.executeUpdate();
          }
          valid = true;
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
      if (valid) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/appointment.fxml"));
        AppointmentController controller = new AppointmentController(cache);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
      }
    }
  }

  /**
   * Validates selected appointment time
   * @param utcStart Selected meeting start
   * @param utcEnd Selected meeting end
   * @param customerId Selected customer
   * @param appointmentId Selected appointment being validated
   * @return Boolean if selected appointment hours are valid or not
   */
  private boolean validateAppointment(Timestamp utcStart, Timestamp utcEnd, int customerId, int appointmentId) {
    ZonedDateTime UtcStart = ZonedDateTime.of(utcStart.toLocalDateTime(), ZoneId.of("UTC"));
    ZonedDateTime EstStart = UtcStart.withZoneSameInstant(ZoneId.of("America/New_York"));
    ZonedDateTime UtcEnd = ZonedDateTime.of(utcEnd.toLocalDateTime(), ZoneId.of("UTC"));
    ZonedDateTime EstEnd = UtcEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

    try {
      String sqlOne = "SELECT * FROM appointments WHERE Customer_ID = '"+customerId+"' AND Appointment_ID != '"+appointmentId+"';";
      Statement stmtOne = DBConnection.open().createStatement();
      ResultSet resultOne = stmtOne.executeQuery(sqlOne);
      String start = "";
      String end = "";
      while (resultOne.next()) {
        start = resultOne.getString("Start");
        end = resultOne.getString("End");
        ZonedDateTime UTCCustomerStart = ZonedDateTime.of(Timestamp.valueOf(start).toLocalDateTime(), ZoneId.of("UTC"));
        ZonedDateTime UTCCustomerEnd = ZonedDateTime.of(Timestamp.valueOf(end).toLocalDateTime(), ZoneId.of("UTC"));
        if ( UTCCustomerStart.getDayOfYear() == UtcStart.getDayOfYear() && UtcStart.compareTo(UTCCustomerStart) < 1 && UtcEnd.compareTo(UTCCustomerEnd)  < 1) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle(rb.getString("scheduleConflict"));
          alert.setHeaderText(rb.getString("customerScheduleConflict"));
          alert.showAndWait();
          return false;
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
    if (EstStart.getHour() > 22 || EstStart.getHour() < 8 || EstEnd.getHour() > 22 || EstEnd.getHour() < 8) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("businessHours"));
      alert.setHeaderText(rb.getString("businessHoursMessage"));
      alert.showAndWait();
      return false;
    }
    return true;
  }

  /**
   * Update available ending minute options
   * @param event End hour
   */
  @FXML
  private void updateDropdown(ActionEvent event) {
    ObservableList<String> endMinutes = FXCollections.observableArrayList();
    ObservableList<String> startMinutes = FXCollections.observableArrayList();
    endMin.getItems().clear();
    String endHourValue = endHour.getValue();
    ZoneId estZone = ZoneId.of("America/New_York");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    LocalDateTime startTime = LocalDateTime.parse("2020-10-31 08:00", formatter);
    LocalDateTime endTime = LocalDateTime.parse("2020-10-31 22:00", formatter);
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
