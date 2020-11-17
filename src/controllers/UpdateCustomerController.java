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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

/**
 * Controller for updating a customer
 */
public class UpdateCustomerController implements Initializable {
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");
  private final Cache cache;
  private final Customer customer;
  private List<String> countries = new ArrayList<>();
  private List<String> divisions = new ArrayList<>();

  @FXML
  private GridPane rootPane;

  @FXML
  private Label modifyCustomerTitle;

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
  private ComboBox<String> countryBoxInput;

  @FXML
  private Label divisionBoxLabel;

  @FXML
  private ComboBox<String> divisionBoxInput;

  @FXML
  private Button saveButton;

  @FXML
  private Button cancelButton;

  /**
   * constructor for update customer
   * @param cache Data store
   * @param customer Selected customer
   */
  public UpdateCustomerController(Cache cache, Customer customer) {
    this.cache = cache;
    this.customer = customer;
  }

  /**
   * initializes window
   * @param url screen url
   * @param resourceBundle text localization resource bundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadLabels();
    loadComboBoxes();
    setData();
  }

  /**
   * Sets input fields with existing data
   */
  private void setData() {
    customerIdInput.setText(Integer.toString(customer.getCustomerId()));
    customerNameInput.setText(customer.getCustomerName());
    addressInput.setText(customer.getAddress());
    postalCodeInput.setText(customer.getPostalCode());
    phoneInput.setText(customer.getPhone());
    divisionBoxInput.setValue(customer.getDivision());
    countryBoxInput.setValue(customer.getCountry());
  }

  /**
   * Loads dropdowns
   */
  private void loadComboBoxes() {
    this.countryBoxInput.getItems().clear();
    this.countries.clear();
    String sqlOne = "SELECT Country_ID, Country FROM countries";
    String sqlTwo = "SELECT Division_ID, Division FROM first_level_divisions d";
    try {
      Statement stmtOne = DBConnection.open().createStatement();
      Statement stmtTwo = DBConnection.open().createStatement();
      ResultSet resultOne = stmtOne.executeQuery(sqlOne);
      ResultSet resultTwo = stmtTwo.executeQuery(sqlTwo);
      while (resultOne.next()) {
        String country = resultOne.getString("Country");
        this.countryBoxInput.getItems().add(country);
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
   * Initializes labels with correct language
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
    saveButton.setText(rb.getString("save"));
    cancelButton.setText(rb.getString("cancel"));
  }

  /**
   * Cancels update customer and sends user back to customer window
   * @param event button click
   * @throws IOException if fails
   */
  @FXML
  private void handleCancel(ActionEvent event) throws IOException {
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
   * Saves customer with the updated information
   * @param event on button click
   */
  @FXML
  void handleSave(ActionEvent event) throws IOException {
    boolean valid = false;
    int newCustomerId = Integer.parseInt(customerIdInput.getText());
    String newCustomerName = customerNameInput.getText();
    String newAddress = addressInput.getText();
    String newPostalCode = postalCodeInput.getText();
    String newPhone = phoneInput.getText();
    Timestamp current_time = new Timestamp( new Date().getTime());
    String last_updated_by = cache.getUser();
    String divisionText = divisionBoxInput.getSelectionModel().getSelectedItem();
    String countryText = countryBoxInput.getSelectionModel().getSelectedItem();

    if (newCustomerName.equals("") || newAddress.equals("") || newPostalCode.equals("")
            || newPhone.equals("") || current_time == null || last_updated_by.equals("") || divisionText.equals("")
            || countryText.equals("")) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("blank"));
      alert.showAndWait();
    }
    else {
      try {
        String rq1 = "SELECT * FROM first_level_divisions WHERE Division = '" + divisionText + "';";
        Statement st1 = DBConnection.open().createStatement();
        ResultSet rs1 = st1.executeQuery(rq1);
        int divisionId = 0;
        while (rs1.next()) {
          divisionId = rs1.getInt("Division_ID");
        }
        String updateSql = "UPDATE customers SET Customer_Name = ?, ADDRESS = ?, Phone = ?, Postal_Code = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement myInsert = DBConnection.open().prepareStatement(updateSql);
        myInsert.setString(1, newCustomerName);
        myInsert.setString(2, newAddress);
        myInsert.setString(3, newPhone);
        myInsert.setString(4, newPostalCode);
        myInsert.setTimestamp(5, current_time);
        myInsert.setString(6, last_updated_by);
        myInsert.setInt(7, divisionId);
        myInsert.setInt(8, newCustomerId);
        customer.setId(newCustomerId);
        customer.setName(newCustomerName);
        customer.setAddress(newAddress);
        customer.setPostalCode(newPostalCode);
        customer.setPhone(newPhone);
        customer.setLastUpdate(current_time);
        customer.setLastUpdatedBy(last_updated_by);
        customer.setDivision(divisionText);
        customer.setCountry(countryText);
        try (var ps = myInsert) {
          ps.executeUpdate();
        }
        valid = true;
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (valid) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/customer.fxml"));
        CustomerController controller = new CustomerController(cache);
        loader.setController(controller);
        stage.setScene(new Scene(loader.load()));
        stage.show();
      }
    }
  }

  /**
   * Updates division dropdown based on selected country
   * @param event Change selected country in dropdown
   */
  @FXML
  private void updateDropdown(ActionEvent event) {
    String selectedCountry = countryBoxInput.getValue();
    this.divisionBoxInput.getItems().removeAll(this.divisions);
    this.divisions.clear();
    String sql = "SELECT d.Division_ID, d.Division, d.COUNTRY_ID, c.Country_ID, c.Country FROM first_level_divisions d JOIN countries c ON (d.COUNTRY_ID = c.Country_ID) WHERE c.Country = '"+selectedCountry+"';";
    try {
      Statement stmtOne = DBConnection.open().prepareStatement(sql);
      ResultSet resultOne = stmtOne.executeQuery(sql);
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
