package model;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * model for appointment
 * @author Ben Paine
 */
public class Appointment {
  private int appointmentId;
  private String title;
  private String description;
  private String location;
  private int contactId;
  private String type;
  private Timestamp start;
  private Timestamp localStart;
  private String formattedLocalStart;
  private Timestamp end;
  private Timestamp localEnd;
  private String formattedLocalEnd;
  private Timestamp createDate;
  private String createdBy;
  private Timestamp lastUpdate;
  private String lastUpdatedBy;
  private int customerId;
  private int userId;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
  /**
   * Creates a new Appointment when created
   * @param appointmentId Appointment ID
   * @param title Appointment title
   * @param description Appointment description
   * @param location Appointment office location
   * @param type Type of appointment
   * @param contactId Contact ID
   * @param start Appointment start time
   * @param end Appointment end time
   * @param createDate Date/time that appointment was created on
   * @param createdBy User that appointment was created by
   * @param lastUpdate Date/time that appointment was last updated by
   * @param lastUpdatedBy User that appointment was last updated by
   * @param customerId Customer ID
   * @param userId User ID
   */
  public Appointment(int appointmentId, String title, String description, String location, String type, int contactId, Timestamp start, Timestamp end, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int customerId, int userId) {
    super();
    setId(appointmentId);
    setName(title);
    setDescription(description);
    setLocation(location);
    setType(type);
    setContactId(contactId);
    setStart(start);
    setEnd(end);
    setLocalStart(start);
    setFormattedLocalStart(start);
    setLocalEnd(end);
    setFormattedLocalEnd(end);
    setCreateDate(createDate);
    setCreatedBy(createdBy);
    setLastUpdate(lastUpdate);
    setLastUpdatedBy(lastUpdatedBy);
    setCustomerId(customerId);
    setUserId(userId);
  }

  /**
   * setter for appointment Id
   * @param appointmentId id
   */
  public void setId(int appointmentId) {
    this.appointmentId = appointmentId;
  }

  /**
   * setter for appointment name
   * @param title name
   */
  public void setName(String title) {
    this.title = title;
  }

  /**
   * Setter for appointment desc
   * @param description des
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Setter for appointment location
   * @param location location
   */
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   * Setter for app type
   * @param type type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * setter for start time
   * @param start start time
   */
  public void setStart(Timestamp start) {
    this.start = start;
  }

  /**
   * sets app in local time zone
   * @param localStart start time
   */
  public void setLocalStart(Timestamp localStart) {
    this.localStart = localStart;
  }

  /**
   * sets time in 12 hour, easily readable format
   * @param localStart time
   */
  public void setFormattedLocalStart(Timestamp localStart) {
    this.formattedLocalStart = localStart.toLocalDateTime().format(formatter);
  }

  /**
   * sets end time
   * @param end time
   */
  public void setEnd(Timestamp end) {
    this.end = end;
  }

  /**
   * sets app in local time zone
   * @param localEnd end time
   */
  public void setLocalEnd(Timestamp localEnd) {
    this.localEnd = localEnd;
  }

  /**
   * sets time to readable format
   * @param localEnd end
   */
  public void setFormattedLocalEnd(Timestamp localEnd) {
    this.formattedLocalEnd = localEnd.toLocalDateTime().format(formatter);
  }

  /**
   * sets create date
   * @param createDate date
   */
  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  /**
   * sets who created the app
   * @param createdBy user
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * sets last updated
   * @param lastUpdate time
   */
  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  /**
   * sets who updated it last
   * @param lastUpdatedBy user
   */
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  /**
   * Gets id of  customer
   * @param customerId id
   */
  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  /**
   * sets user id
   * @param userId id
   */
  public void setUserId(int userId) {
    this.userId = userId;
  }

  /**
   * sets contact id
   * @param contactId id
   */
  public void setContactId(int contactId) {
    this.contactId = contactId;
  }

  /**
   * gets app id
   * @return id
   */
  public int getAppointmentId() {
    return appointmentId;
  }

  /**
   * gets app title
   * @return title
   */
  public String getTitle() {
    return title;
  }

  /**
   * gets appointment desc
   * @return description
   */
  public String getDescription() {
    return description;
  }

  /**
   * gets location
   * @return location
   */
  public String getLocation() {
    return location;
  }

  /**
   * gets type
   * @return type
   */
  public String getType() {
    return type;
  }

  /**
   * gets starting time
   * @return start
   */
  public Timestamp getStart() {
    return start;
  }

  /**
   * gets start in local time zone
   * @return start time
   */
  public Timestamp getLocalStart() {
    return localStart;
  }

  /**
   * gets formatted local start
   * @return time
   */
  public String getFormattedLocalStart() {
    return formattedLocalStart;
  }

  /**
   * gets ending time
   * @return time
   */
  public Timestamp getEnd() {
    return end;
  }

  /**
   * gets local ending time
   * @return time
   */
  public Timestamp getLocalEnd() {
    return localEnd;
  }

  /**
   * gets formatted local time
   * @return end time
   */
  public String getFormattedLocalEnd() {
    return formattedLocalEnd;
  }

  /**
   * gets date and time it was created
   * @return date and time
   */
  public Timestamp getCreateDate() {
    return createDate;
  }

  /**
   * gets user who created app
   * @return user
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * gets date and time last updated
   * @return last updated
   */
  public Timestamp getLastUpdate() {
    return lastUpdate;
  }

  /**
   * gets user who updated it last
   * @return user
   */
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  /**
   * gets id of user that created app
   * @return user id
   */
  public int getUserId() {
    return userId;
  }

  /**
   * gets id of contact
   * @return contact id
   */
  public int getContactId() {
    return contactId;
  }

  /**
   * gets id of customer
   * @return customer ID
   */
  public int getCustomerId() {
    return customerId;
  }
}
