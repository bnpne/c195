package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that connects to the database
 */
public class DBConnection {
  // Database Information
  private static final String databaseName="WJ086b2";
  private static final String DB_URL="jdbc:mysql://wgudb.ucertify.com/" + databaseName + "?useSSL=false";
  private static final String username="U086b2";
  private static final String password="53689213762";
  public static Connection conn;

  /**
   * Opens connection to the database
   * @return the connection so data can be accessed
   * @throws SQLException exception if fails
   */
  public static Connection open() throws SQLException {
    conn = DriverManager.getConnection(DB_URL, username, password);
    return conn;
  }

  /**
   * Closes connection to the database
   * @throws SQLException exception if fails
   */
  public static void close() throws SQLException {
    conn.close();
  }
}
