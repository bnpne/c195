package controllers;

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
import model.Cache;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for new customer screen
 * @author Ben Paine
 */
public class AddCustomerController implements Initializable {
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
  private final Cache cache;
  private List<String> countries = new ArrayList<>();
  private List<String> divisions = new ArrayList<>();

  @FXML
  private GridPane rootPane;

  @FXML
  private Label customerIdLabel;

  @FXML
  private TextField customerIdInput;

  @FXML
  private Label customerNameLabel;

  @FXML
  private TextField customerNameInput;

  @FXML
  private Label addressLabel;

  @FXML
  private TextField addressInput;

  @FXML
  private Label postalCodeLabel;

  @FXML
  private TextField postalCodeInput;

  @FXML
  private Label phoneLabel;

  @FXML
  private TextField phoneInput;

  @FXML
  private Label countryLabel;

  @FXML
  private ComboBox<String> countryTab;

  @FXML
  private Label divisionBoxLabel;

  @FXML
  private ComboBox<String> divisionBoxInput;

  @FXML
  private Button saveCustomer;

  @FXML
  private Button cancelCustomer;

  /**
   * Controller for customer
   * @param cache Data store with local session data
   */
  public AddCustomerController(Cache cache) {
    this.cache = cache;
  }
  /**
   * Initializes screen
   * @param url url
   * @param resourceBundle resource bundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadLabels();
    loadBoxes();
  }

  /**
   * Loads dropdown boxes
   */
  private void loadBoxes() {
    this.countryTab.getItems().clear();
    this.countries.clear();
    String sqlOne = "SELECT Country FROM countries";
    String sqlTwo = "SELECT Division FROM first_level_divisions d";
    try {
      Statement stmtOne = DBConnection.open().createStatement();
      Statement stmtTwo = DBConnection.open().createStatement();
      ResultSet resultOne = stmtOne.executeQuery(sqlOne);
      ResultSet resultTwo = stmtTwo.executeQuery(sqlTwo);
      while (resultOne.next()) {
        String country = resultOne.getString("Country");
        this.countryTab.getItems().add(country);
        this.countries.add(country);
      }
      while (resultTwo.next()) {
        String division = resultTwo.getString("Division");
        this.divisionBoxInput.getItems().add(division);
        this.divisions.add(division);
      }

    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads label with correct language
   */
  private void loadLabels() {
    customerIdLabel.setText(rb.getString("id"));
    customerNameLabel.setText(rb.getString("name"));
    addressLabel.setText(rb.getString("address"));
    postalCodeLabel.setText(rb.getString("postalCode"));
    phoneLabel.setText(rb.getString("phoneNumber"));
    countryLabel.setText(rb.getString("country"));
    divisionBoxLabel.setText(rb.getString("division"));
    customerIdInput.setPromptText(rb.getString("disabledAutoGen"));
    customerNameInput.setPromptText(rb.getString("customerName"));
    addressInput.setPromptText(rb.getString("address"));
    postalCodeInput.setPromptText(rb.getString("postalCode"));
    phoneInput.setPromptText(rb.getString("phoneNumber"));
    saveCustomer.setText(rb.getString("save"));
    cancelCustomer.setText(rb.getString("cancel"));
  }

  /**
   * Cancels customer update
   * @param event button click
   * @throws IOException if fails
   */
  @FXML
  void handleCancel(ActionEvent event) throws IOException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle(rb.getString("cancelConfirmation"));
    alert.setContentText(rb.getString("cancelCustomerMessage"));
    Optional<ButtonType> result = alert.showAndWait();
    if (result.get() == ButtonType.OK){
      Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/customer.fxml"));
      CustomerController controller = new CustomerController(cache);
      loader.setController(controller);
      Scene scene = new Scene(loader.load());
      stage.setScene(scene);
      stage.show();
    }
  }

  /**
   * Increments and creates a new id. so the database doesnt have to be sorted
   * @return id
   * @throws SQLException if fails
   */
  public static int getNewId() throws SQLException {
    String sql = "SELECT * FROM customers";
    Statement stmt = DBConnection.open().createStatement();
    ResultSet result = stmt.executeQuery(sql);
    int count = 0;
    while (result.next()) {
      if (result.getInt("Customer_ID") > count) count = result.getInt("Customer_ID");
    }
    DBConnection.close();
    return count + 1;
  }

  /**
   * Saves customer
   * @param event button click
   */
  @FXML
  void handleSave(ActionEvent event) throws SQLException, IOException {
    boolean valid = true;
    int newId = getNewId();

    String customerNameText = customerNameInput.getText();
    String addressText = addressInput.getText();
    String postalCodeText = postalCodeInput.getText();
    String phoneText = phoneInput.getText();
    java.sql.Date current_date = new java.sql.Date(System.currentTimeMillis());
    String created_by = cache.getUser();
    Timestamp current_time = new Timestamp( new java.sql.Date(System.currentTimeMillis()).getTime());
    String last_updated_by = cache.getUser();
    String divisionText = divisionBoxInput.getSelectionModel().getSelectedItem();
    String countryText = countryTab.getSelectionModel().getSelectedItem();

    if (customerNameText.equals("") || addressText.equals("") || postalCodeText.equals("") || phoneText.equals("") || current_date == null ||
            created_by.equals("") || current_date == null || last_updated_by.equals("") || divisionText.equals("") || countryText.equals("")) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("blank"));
      alert.showAndWait();
    }
    else {
      Customer customer = new Customer(newId, customerNameText, addressText, postalCodeText, phoneText, current_date, created_by, current_time, last_updated_by, divisionText, countryText);
      try {
        String sqlOne = "SELECT * FROM first_level_divisions WHERE Division = '" + divisionText + "';";
        Statement stmtOne = DBConnection.open().createStatement();
        ResultSet resultOne = stmtOne.executeQuery(sqlOne);
        int divisionId = 0;
        while (resultOne.next()) {
          divisionId = resultOne.getInt("Division_ID");
        }
        String sqlTwo = "INSERT INTO customers VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement insert = DBConnection.open().prepareStatement(sqlTwo);
        insert.setInt(1, newId);
        insert.setString(2, customerNameText);
        insert.setString(3, addressText);
        insert.setString(4, postalCodeText);
        insert.setString(5, phoneText);
        insert.setDate(6, current_date);
        insert.setString(7, created_by);
        insert.setTimestamp(8, current_time);
        insert.setString(9, last_updated_by);
        insert.setInt(10, divisionId);
        try (var ps = insert) {
          ps.executeUpdate();
        }
        cache.addCustomer(customer);
      } catch (Exception e) {
        e.printStackTrace();
      }
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));

      if (valid) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/customer.fxml"));
        CustomerController controller = new CustomerController(cache);
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.show();
      } else {
        alert.showAndWait();
      }
    }
  }

  /**
   * Updates dropdown
   * @param event button click
   */
  @FXML
  void updateDropdown(ActionEvent event) {
    String selectedCountry = countryTab.getValue();
    String sql = "SELECT d.Division_ID, d.Division, d.COUNTRY_ID, c.Country_ID, c.Country FROM first_level_divisions d JOIN countries c ON (d.COUNTRY_ID = c.Country_ID) WHERE c.Country = '"+selectedCountry+"';";
    try {
      Statement stmtOne = DBConnection.open().createStatement();
      ResultSet resultOne = stmtOne.executeQuery(sql);
      this.divisionBoxInput.getItems().removeAll(this.divisions);
      this.divisions.clear();
      while (resultOne.next()) {
        String division = resultOne.getString("Division");
        this.divisionBoxInput.getItems().add(division);
        this.divisions.add(division);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

}
