package model;

/**
 * Contact for an appointment
 * @author Ben Paine
 */
public class Contact {
  private int contactId;
  private String contactName;
  private String email;

  /**
   * Creates a contact
   * @param contactId Contact ID
   * @param contactName Name of contact
   * @param email Email of contact
   */
  public Contact(int contactId, String contactName, String email) {
    super();
    setId(contactId);
    setName(contactName);
    setEmail(email);
  }

  /**
   * sets contact dd
   * @param contactId id
   */
  public void setId(int contactId) {
    this.contactId = contactId;
  }

  /**
   * sets contact name
   * @param contactName name
   */
  public void setName(String contactName) {
    this.contactName = contactName;
  }

  /**
   * sets contact email
   * @param email email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * sets contact id
   * @return Contact id
   */
  public int getId() {
    return contactId;
  }

  /**
   * gets contact name
   * @return name of contact
   */
  public String getName() {
    return contactName;
  }

  /**
   * gets contact email
   * @return email of contact
   */
  public String getEmail() {
    return email;
  }
}
