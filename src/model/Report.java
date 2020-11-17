package model;

import javafx.beans.value.ObservableValue;

/**
 * Report model for appointments
 * @author Ben Paine
 */
public class Report {
  private ObservableValue<Integer> count;
  private ObservableValue<String> attribute;

  /**
   * @param count Number of appointments
   * @param attribute attribute of interest
   */
  public Report(ObservableValue<Integer> count, ObservableValue<String> attribute) {
    super();
    setCount(count);
    setAttribute(attribute);
  }

  /**
   * sets number of app count
   * @param count number of appt
   */
  public void setCount(ObservableValue<Integer> count) {
    this.count = count;
  }

  /**
   * set attribute to compare against
   * @param attribute attribute
   */
  public void setAttribute(ObservableValue<String> attribute) {
    this.attribute = attribute;
  }

  /**
   * gets count
   * @return number
   */
  public ObservableValue<Integer> getCount() {
    return count;
  }

  /**
   * gets attribute
   * @return attribute
   */
  public ObservableValue<String> getAttribute() {
    return attribute;
  }
}
