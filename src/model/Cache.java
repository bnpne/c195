package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Locale;

/**
 * Cache of local data used for easy access without having to access database every time
 * @author Ben Paine
 */
public class  Cache {
  private String user;
  private Locale locale;
  private ObservableList<Customer> allCustomers;
  private ObservableList<Contact> allContacts;
  private ObservableList<Appointment> allAppointments;

  /**
   * creates and loads new cache
   */
  public Cache() {
    allCustomers = FXCollections.observableArrayList();
    allContacts = FXCollections.observableArrayList();
    allAppointments = FXCollections.observableArrayList();
  }

  /**
   * adds logged in user
   * @param auth user logged in
   */
  public void addUser(String auth) {
    if (auth != null) {
      user = auth;
    }
  }

  /**
   * adds new contact
   * @param newContact new contact
   */
  public void addContact(Contact newContact) {
    if (newContact != null) {
      allContacts.add(newContact);
    }
  }

  /**
   * adds new customer
   * @param newCustomer new customer
   */
  public void addCustomer(Customer newCustomer) {
    if (newCustomer != null) {
      allCustomers.add(newCustomer);
    }
  }

  /**
   * adds new appointment
   * @param newAppointment app
   */
  public void addAppointment(Appointment newAppointment) {
    if (newAppointment != null) {
      allAppointments.add(newAppointment);
    }
  }

  /**
   * updates local time of user
   * @param newLocale local code
   */
  public void updateLocale(Locale newLocale) {
    if (newLocale != null) {
      locale = newLocale;
    }
  }

  /**
   * gets logged in user locale
   * @return locale of user
   */
  public Locale getLocale() {
    return locale;
  }

  /**
   * gets user name
   * @return user name
   */
  public String getUser() {
    return user;
  }

  /**
   * gets list of all customers
   * @return all customers
   */
  public ObservableList<Customer> getAllCustomers() {
    return allCustomers;
  }

  /**
   * gets list of all contacts
   * @return all contacts
   */
  public ObservableList<Contact> getAllContacts() {
    return allContacts;
  }

  /**
   * gets list of all appointments
   * @return all appointments
   */
  public ObservableList<Appointment> getAllAppointments() {
    return allAppointments;
  }
}
