package lib;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * Used to convert time
 */
public class Time {
  /**
   * Converts time to UTC
   * @param utcTime selected month
   * @param hour selected hour
   * @param minute selected minute
   * @return returned time
   */
  public Timestamp convertTimeToUTC(LocalDate utcTime, String hour, String minute) {
    LocalDateTime ldt = LocalDateTime.of(utcTime.getYear(), utcTime.getMonthValue(), utcTime.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));
    ZonedDateTime local = ZonedDateTime.of(ldt, ZoneId.systemDefault());
    ZonedDateTime utc = local.withZoneSameInstant(ZoneOffset.UTC);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return Timestamp.valueOf(utc.format(formatter));
  }

  /**
   * Converts time that user chooses to readable time
   * @param utcTime utc time
   * @param hour selected hour
   * @param minute selected minute
   * @return returned time
   */
  public Timestamp convertTimeToLocal(LocalDate utcTime, String hour, String minute) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd KK:mm:ss a");
    LocalDateTime ldt = LocalDateTime.of(utcTime.getYear(), utcTime.getMonthValue(), utcTime.getDayOfMonth(), Integer.parseInt(hour), Integer.parseInt(minute));
    return Timestamp.valueOf(ldt);
  }
}
