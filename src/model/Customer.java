package model;

import java.util.Date;
import java.sql.Timestamp;

/**
 * customer model for appointment
 * @author Ben Paine
 */
public class Customer {
  private int customerId;
  private String customerName;
  private String address;
  private String postalCode;
  private String phone;
  private Date createDate;
  private String createdBy;
  private Timestamp lastUpdate;
  private String lastUpdatedBy;
  private String division;
  private String country;

  /**
   * Create customer
   * @param customerId Customer ID
   * @param customerName Customer name
   * @param address Customer address
   * @param postalCode Customer postal code
   * @param phone Customer phone number
   * @param createDate Date/time that customer was created on
   * @param createdBy User that customer was created by
   * @param lastUpdate Date/time that customer was last updated by
   * @param lastUpdatedBy User that last updated customer information
   * @param division Customer division
   * @param country Customer country
   */
  public Customer(int customerId, String customerName, String address, String postalCode, String phone, Date createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, String division, String country) {
    super();
    setId(customerId);
    setName(customerName);
    setAddress(address);
    setPostalCode(postalCode);
    setPhone(phone);
    setCreateDate(createDate);
    setCreatedBy(createdBy);
    setLastUpdate(lastUpdate);
    setLastUpdatedBy(lastUpdatedBy);
    setDivision(division);
    setCountry(country);
  }

  /**
   * sets customer id
   * @param customerId id
   */
  public void setId(int customerId) {
    this.customerId = customerId;
  }

  /**
   * sets customer name
   * @param customerName name
   */
  public void setName(String customerName) {
    this.customerName = customerName;
  }

  /**
   * sets customer address
   * @param address address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * sets customer postal code
   * @param postalCode postal code
   */
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  /**
   * sets customer phone number
   * @param phone phone number
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * sets date and time customer was created
   * @param createDate date and time
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  /**
   * sets user that customer was created by
   * @param createdBy user
   */
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Sets date and time customer was updated
   * @param lastUpdate date and time
   */
  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  /**
   * sets user that updated customer
   * @param lastUpdatedBy user
   */
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  /**
   * sets customer division
   * @param division division
   */
  public void setDivision(String division) {
    this.division = division;
  }

  /**
   * sets country
   * @param country country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * gets id
   * @return ID
   */
  public int getCustomerId() {
    return customerId;
  }

  /**
   * Get name
   * @return name
   */
  public String getCustomerName() {
    return customerName;
  }

  /**
   * Get address
   * @return address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Get postal code
   * @return postal code
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Get phone number
   * @return phone number
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Get date and time customer was created
   * @return date and time
   */
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * Gets user
   * @return User that customer was created by
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Gets date and time customer was updated
   * @return date and time
   */
  public Timestamp getLastUpdate() {
    return lastUpdate;
  }

  /**
   * gets user who updated customer
   * @return user
   */
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  /**
   * gets division
   * @return division
   */
  public String getDivision() {
    return division;
  }

  /**
   * gets country
   * @return country
   */
  public String getCountry() {
    return country;
  }
}
