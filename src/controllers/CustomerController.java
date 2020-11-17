package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lib.DBConnection;
import model.Cache;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for Customer window. Displays customers
 */
public class CustomerController implements Initializable {

  private final Cache cache;
  private ResourceBundle rb = ResourceBundle.getBundle("language_files/rb");

  @FXML
  private GridPane rootPane;

  @FXML
  private TableView<Customer> customerTable;

  @FXML
  private TableColumn<?, ?> customerID;

  @FXML
  private TableColumn<?, ?> customerName;

  @FXML
  private TableColumn<?, ?> customerAddress;

  @FXML
  private TableColumn<?, ?> postalCode;

  @FXML
  private TableColumn<?, ?> phoneNumber;

  @FXML
  private TableColumn<?, ?> divisionId;

  @FXML
  private TableColumn<?, ?> country;

  @FXML
  private Button addCustomer;

  @FXML
  private Button updateCustomer;

  @FXML
  private Button deleteCustomer;

  @FXML
  private Button backButton;

  /**
   * constructor for the customer
   * @param cache Data store with local session data
   */
  public CustomerController(Cache cache) {
    this.cache = cache;
  }

  /**
   * Initializes screen
   * @param url screen url
   * @param resourceBundle text localization resource bundle
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    loadLabels();
    customerTable.setItems(cache.getAllCustomers());
  }

  /**
   * Loads label in correct language
   */
  private void loadLabels() {
    customerID.setText(rb.getString("id"));
    customerName.setText(rb.getString("name"));
    customerAddress.setText(rb.getString("address"));
    postalCode.setText(rb.getString("postalCode"));
    phoneNumber.setText(rb.getString("phoneNumber"));
    divisionId.setText(rb.getString("division"));
    country.setText(rb.getString("country"));
    addCustomer.setText(rb.getString("add"));
    updateCustomer.setText(rb.getString("update"));
    deleteCustomer.setText(rb.getString("delete"));
    backButton.setText(rb.getString("back"));
  }

  /**
   * Loads add customer window
   * @param event button click
   * @throws IOException if fails
   */
  @FXML
  void handleAddCustomer(ActionEvent event) throws IOException {
    Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/addcustomer.fxml"));
    AddCustomerController controller = new AddCustomerController(cache);
    loader.setController(controller);
    stage.setScene(new Scene(loader.load()));
    stage.show();
  }

  /**
   * Goes back to appointment window
   * @param event on button click
   * @throws IOException if fails
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
   * Deletes customer that is selected
   * @param event on button click
   */
  @FXML
  void handleDeleteCustomer(ActionEvent event) {
    ObservableList<Customer> data = FXCollections.observableArrayList();
    if (customerTable.getSelectionModel().getSelectedItem() != null) {
      data = customerTable.getItems();
      try {
        var sql = "DELETE FROM customers WHERE Customer_ID = "+customerTable.getSelectionModel().getSelectedItem().getCustomerId();
        try (var ps = DBConnection.open().prepareStatement(sql)) {
          ps.executeUpdate();
        }
        data.remove(customerTable.getSelectionModel().getSelectedItem());
        customerTable.setItems(data);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(rb.getString("customerDeleteTitle"));
        alert.setHeaderText(rb.getString("customerDeleteTitle"));
        alert.setContentText(rb.getString("customerDeleteMessage"));
        alert.showAndWait();
      } catch(SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(rb.getString("error"));
        alert.setHeaderText(rb.getString("customerDeleteErrorTitle"));
        alert.setContentText(rb.getString("customerDeleteErrorMessage"));
        alert.showAndWait();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("customerNotFound"));
      alert.showAndWait();
    }
  }

  /**
   * sends user to customer update window
   * @param event Button click
   * @throws IOException if fails
   */
  @FXML
  private void handleUpdateCustomer(ActionEvent event) throws IOException {
    Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
    if (selectedCustomer != null) {
      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/updatecustomer.fxml"));
      UpdateCustomerController controller = new UpdateCustomerController(cache, selectedCustomer);
      loader.setController(controller);
      stage.setScene(new Scene(loader.load()));
      stage.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle(rb.getString("error"));
      alert.setHeaderText(rb.getString("error"));
      alert.setContentText(rb.getString("selectCustomer"));
      alert.showAndWait();
    }
  }

}

