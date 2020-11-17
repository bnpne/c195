package controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Appointment;
import model.Cache;
import model.Report;

import java.io.IOException;
import java.net.URL;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Controller for report screen
 */
public class ReportController implements Initializable {
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
  private final Cache cache;
  ObservableList<Report> monthList, locationList;
  private List<String> contacts = new ArrayList<>();

  @FXML
  private GridPane rootPane;

  @FXML
  private Tab ReportsByMonth;

  @FXML
  private TableView<Report> typeTotalTable;

  @FXML
  private TableColumn<Report, String> colType;

  @FXML
  private TableColumn<Report, Integer> colTypeTotal;

  @FXML
  private Tab ReportsContactByMonth;

  @FXML
  private TableView<Report> monthTotalTable;

  @FXML
  private TableColumn<Report, String> colMonth;

  @FXML
  private TableColumn<Report, Integer> colMonthTotal;

  @FXML
  private Tab locationReports;

  @FXML
  private TableView<Report> locationTableView;

  @FXML
  private TableColumn<Report, String> colLocation;

  @FXML
  private TableColumn<Report, Integer> colLocationTotal;

  @FXML
  private Tab ReportsSchedule;

  @FXML
  private ComboBox<String> contactBoxInput;

  @FXML
  private TableView<Appointment> appointmentTable;

  @FXML
  private TableColumn<Appointment, Integer> appointmentId;

  @FXML
  private TableColumn<Appointment, Integer> title;

  @FXML
  private TableColumn<Appointment, String> description;

  @FXML
  private TableColumn<Appointment, String> type;

  @FXML
  private TableColumn<Appointment, Date> startTime;

  @FXML
  private TableColumn<Appointment, Date> endTime;

  @FXML
  private TableColumn<Appointment, Date> customerId;

  @FXML
  private Button backButton;

  /**
   * Controller constructor
   * @param cache main local data
   */
  public ReportController(Cache cache) {
    this.cache = cache;
  }

  /**
   * Initializes tables with data from database and with correct language
   * @param url the FXML file
   * @param resourceBundle the language
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    appointmentId.setText(rb.getString("id"));
    title.setText(rb.getString("title"));
    description.setText(rb.getString("description"));
    type.setText(rb.getString("type"));
    startTime.setText(rb.getString("startTime"));
    endTime.setText(rb.getString("endTime"));
    customerId.setText(rb.getString("customerId"));
    backButton.setText(rb.getString("back"));

    loadMonthList();
    loadLocationList();
    loadAppointmentContacts();
  }

  /**
   * Load all contacts
   */
  private void loadAppointmentContacts() {
    this.contactBoxInput.getItems().clear();
    this.contacts.clear();
    contactBoxInput.getItems().add(rb.getString("any"));
    contactBoxInput.setValue(rb.getString("any"));
    for (int i = 0; i < cache.getAllContacts().size(); i++ ) {
      contactBoxInput.getItems().add(cache.getAllContacts().get(i).getName());
      this.contacts.add(cache.getAllContacts().get(i).getName());
    }
    appointmentTable.setItems(cache.getAllAppointments());
  }

  /**
   * Load location table
   * Lambda function to improve efficiency  this function helps the data be filtered by contact
   *  returns correct row and location list
   */
  private void loadLocationList() {
    locationList = FXCollections.observableArrayList();
    Map<String, Integer> locationMap = new HashMap<String, Integer>();
    String location = "";
    int count;

    // TODO: FIX THIS
    for (int i = 0; i < cache.getAllAppointments().size(); i++) {
      location = cache.getAllAppointments().get(i).getLocation();
      count = 0;

      if (!locationMap.containsKey(location)) {
        for (int j = 0; j < cache.getAllAppointments().size(); j++) {
          if (cache.getAllAppointments().get(j).getLocation().equals(location)) {
            count++;
          }
        }
        locationMap.put(location, count);
      }
    }

    for (Map.Entry<String, Integer> mapElement : locationMap.entrySet()) {
      locationList.add(new Report(
              new ReadOnlyObjectWrapper<>(mapElement.getValue()),
              new ReadOnlyStringWrapper(mapElement.getKey())
      ));
    }

    /**
     * Lambda function to improve efficiency  this function helps the data be filtered by contact
     */
    colLocation.setCellValueFactory(cellData -> {
      return cellData.getValue().getAttribute();
    });
    colLocationTotal.setCellValueFactory(cellData -> {
      return cellData.getValue().getCount();
    });
    locationTableView.setItems(locationList);
  }

  /**
   * Loads month table
   */
  private void loadMonthList() {
    monthList = FXCollections.observableArrayList();
    Map<String, Integer> typesInMonth = new HashMap<String, Integer>();
    String month = "";
    for (int i = 0; i < cache.getAllAppointments().size(); i++) {
      Locale locale = Locale.getDefault();
      month = cache.getAllAppointments().get(i).getLocalStart().toLocalDateTime().getMonth().getDisplayName(TextStyle.FULL, locale);

      if (!typesInMonth.containsKey(month)) {
        ArrayList<String> typeList = new ArrayList<>()  ;
        for (int j = 0; j < cache.getAllAppointments().size(); j++) {
          if (cache.getAllAppointments().get(j).getLocalStart().toLocalDateTime().getMonth().getDisplayName(TextStyle.FULL, locale).equals(month)) {
            if (!typeList.contains(cache.getAllAppointments().get(j).getType())) {
              typeList.add(cache.getAllAppointments().get(j).getType());
            }
          }
        }

        typesInMonth.put(month, typeList.size());
      }
    }

    for (Map.Entry<String, Integer> mapElement : typesInMonth.entrySet()) {

      monthList.add(new Report(
              new ReadOnlyObjectWrapper<>(mapElement.getValue()),
              new ReadOnlyStringWrapper(mapElement.getKey())
      ));
    }

    /**
     * Lambda function to improve efficiency. this function helps the data be filtered by contact
     */
    colMonth.setCellValueFactory(cellData -> {
      return cellData.getValue().getAttribute();
    });
    colTypeTotal.setCellValueFactory(cellData -> {
      return cellData.getValue().getCount();
    });
    typeTotalTable.setItems(monthList);
  }

  /**
   * Goes back to the appointment page
   * @param event on button click
   */
  @FXML
  void handleBack(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/appointment.fxml"));
    AppointmentController controller = new AppointmentController(cache);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }

  /**
   * toggles contact drop down
   * Lambda function to improve efficiency. this function helps the data be filtered by contact
   * returns correct appointment by type
   * @param event dropdown
   */
  @FXML
  void handleContactBox(ActionEvent event) {
    String selectedContact = contactBoxInput.getSelectionModel().getSelectedItem();
    FilteredList<Appointment> filteredData = new FilteredList<>(cache.getAllAppointments());
    /**
     * Lambda function to improve efficiency. this function helps the data be filtered by contact
     */
    filteredData.setPredicate(row -> {
      Integer contactId = row.getContactId();
      return selectedContact.equals("Any") || selectedContact.equals(cache.getAllContacts().get(contactId - 1).getName());
    });
    if (selectedContact.equals("Any")) {
      appointmentTable.setItems(cache.getAllAppointments());
    }
    else {
      appointmentTable.setItems(filteredData);
    }
  }

}
