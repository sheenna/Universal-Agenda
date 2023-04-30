package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * This is the DatabaseHelper class. It handles all the database connections and queries, as well as some other miscellaneous database-related functions.
 * <p> It is important to note that the actual loading of the Appointment and Customer tableviews are in the dao package.</p>
 */
public class DatabaseHelper {
    /**
     * The constant protocol.
     */
    private static final String protocol = "jdbc";
    /**
     * The constant vendor.
     */
    private static final String vendor = ":mysql:";
    /**
     * The constant location.
     */
    private static final String location = "//localhost/";
    /**
     * The constant databaseName.
     */
    private static final String databaseName = "client_schedule";
    /**
     * The constant jdbcUrl.
     */
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone=SERVER"; // LOCAL
    /**
     * The constant driver.
     */
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    /**
     * The constant database username. This is not to be confused with the user/username of the program.
     */
    private static final String userName = "sqlUser"; // Username
    /**
     * The constant database password.
     */
    private static final String password = "Passw0rd!"; // Password
    /**
     * The constant connection.
     */
    private static Connection connection = null;

    /**
     * This is a static method that connects to the database.
     *
     * @return the connection
     */

// Connect to the database
    public static Connection connect() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            return connection;
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
            return null;
        }
    }

    /**
     * This is a static <b>void</b> method that connects to the database.
     */
    public static void openConnection() {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * This static method runs a query depending on the String passed to it. It returns a ResultSet to be processed by method calling it.
     * I used this primarily for SELECT queries.
     *
     * @param query String query.
     * @return the result set
     */
    public static ResultSet executeQuery(String query) {
        ResultSet rs = null;
        try {
            Statement stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
        return rs;
    }

    /**
     * This static method runs a query depending on the String passed to it.
     * It returns an integer value from the database, which is the number of rows affected by the query.
     * This is used primarily for INSERT, UPDATE, and DELETE queries.
     *
     * @param query String query
     * @return the number of rows affected by the query
     */
    public static int executeUpdate(String query) {
        int result = 0;
        try {
            Statement stmt = connection.createStatement();
            result = stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
        return result;
    }

    /**
     * This static method closes the connection to the database.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }



    /**
     * This method would return names and IDs for a given table.
     * Arguments: table name, first column name, second column name.
     * This is mostly used for populating the ComboBoxes in some of the Add and Update screens.
     * It returns an ObservableList of Strings.
     * It can be used for multiple purposes, but I mostly used it to retrieve the names and IDs of contacts, customers, and users.
     *
     * @param tableName        the table name
     * @param firstColumnName  the first column name
     * @param secondColumnName the second column name
     * @return an ObservableList of Strings with values from the first column and IDs from the second column, formatted as Name ID: #
     */
    public static ObservableList<String> getNamesAndIDs(String tableName, String firstColumnName, String secondColumnName) {
        ObservableList<String> columnNamesWithIds = FXCollections.observableArrayList();
        Connection conn = connect();
        String query = "SELECT " + secondColumnName + ", " + firstColumnName + " FROM " + tableName;

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String columnNameWithId = rs.getString(firstColumnName) + " ID: " + rs.getInt(secondColumnName);
                columnNamesWithIds.add(columnNameWithId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return columnNamesWithIds;
    }

    /**
     * This method pairs with the getNamesAndIDs method.
     * It takes a given String and parses out the ID, considering the format of the previous method.
     *
     * @param selection String selection
     * @return integer ID
     */
    public static int identifyIDs(String selection) {
        String idString = selection.substring(selection.indexOf("ID: ") + 4);
        return Integer.parseInt(idString);
    }


    /**
     * This method is used to authenticate users.
     * It takes the username String and the password String from the login screen, and checks them against the database.
     * It also logs the result of the authentication by accessing a method in the LogHelper class.
     * PRovides pop-up alerts if the authentication fails.
     *
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    public static boolean authenticate(String username, String password) {
        try {
            // create the SQL statement
            String sql = "SELECT * FROM client_schedule.users WHERE User_Name = ? AND Password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            // execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // check if there is a match
            boolean isAuthenticated = resultSet.next();

            // log the authentication result
            LogHelper logHelper = new LogHelper();
            if (isAuthenticated) {
                logHelper.log("User \"" + username + "\" has been authenticated successfully.");
            } else {
                logHelper.log("Authentication failed for user \"" + username + "\"");
            }

            return isAuthenticated;
        } catch (SQLException e) {
            LogHelper logHelper = new LogHelper();
            logHelper.log("Error occurred while authenticating user \"" + username + "\": " + e.getMessage());
            return false;
        }
    }


}
