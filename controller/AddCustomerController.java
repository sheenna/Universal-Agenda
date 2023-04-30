package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import utilities.DatabaseHelper;
import utilities.GeoHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

/**
 * The Add Customer Controller.
 */
public class AddCustomerController {
    /**
     * This label updates whenever the user selects a new locale. It's not fancy, but it may be my favorite part.
     */
    public Label actualDivID;
    /**
     * The Division ID.
     */
    int divisionID;
    /**
     * The Open ID. This is the next available customer ID.
     */
    int openID;
    /**
     * The Customer ID field.
     */
    @FXML
    private TextField customerIDField;
    /**
     * The Customer name field.
     */
    @FXML
    private TextField customerNameField;
    /**
     * The Customer address field.
     */
    @FXML
    private TextField customerAddressField;
    /**
     * The Customer phone field.
     */
    @FXML
    private TextField customerPhoneField;
    /**
     * The Customer postal code field.
     */
    @FXML
    private TextField customerPostalCodeField;
    /**
     * The Country chooser.
     */
    @FXML
    private ComboBox<String> countryChooser;
    /**
     * The Division chooser.
     */
    @FXML
    private ComboBox<String> divisionChooser;
    /**
     * The Save button.
     */
    @FXML
    private Button saveButton;
    /**
     * The Back button.
     */
    @FXML
    private Button backButton;

    /**
     * Gets next customer ID by querying the database for the highest customer ID present in the table and adding 1.
     *
     * @return the next customer ID
     * @throws SQLException the sql exception
     */
    public static int getNextCustomerId() throws SQLException {
        String query = "SELECT MAX(Customer_ID) FROM client_schedule.customers;";
        ResultSet rs = DatabaseHelper.executeQuery(query);
        if (rs.next()) {
            int openID = rs.getInt(1);
            return openID + 1;
        } else {
            throw new SQLException("Unable to get the next Customer ID.");
        }
    }

    /**
     * Gets key by value.
     * This is for the division ID look-up.
     *
     * @param map   the map
     * @param value the value
     * @return the key by value
     */
    public static int getKeyByValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Initialize method.
     * This fills the country chooser, then sets up an ADDLISTENER to change the division chooser when the country chooser changes.
     * THIS IS ANOTHER LAMBDA. There are two AddListener methods here, meaning two Lambdas.
     * Basically uses the combobox values as parameters to execute the change in the division chooser.
     * The second one grabs the division ID from the map in GeoHelper, after listening for a change in the division chooser.
     *
     * @throws SQLException the sql exception
     * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
     * It's been incredibly useful to use the LAMBDA EXPRESSIONS for addListeners, especially considering without them, the code would be a lot messier and more difficult to read.
     * They're used for anonymous expressions without having to define a whole Class for them, and they're
     * especially useful in conjunction with Java's built-in methods.
     *
     */
    @FXML
    private void initialize() throws SQLException {
        countryChooser.setItems(FXCollections.observableArrayList(GeoHelper.COUNTRIES));

        // Set up divisionChooser to change when countryChooser changes
        //LAMBDA EXPRESSION
        countryChooser.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                divisionChooser.setItems(null);
            } else if (newValue.equals("US")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.US_DIVISION_1));
            } else if (newValue.equals("UK")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.UK_DIVISION_2));
            } else if (newValue.equals("Canada")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.CANADA_DIVISION_3));
            }
        });

        // Fill divisionChooser with the appropriate options based on the initial selection in countryChooser
        handleCountryChooser();
        openID = getNextCustomerId();
        customerIDField.setText(Integer.toString(openID));

        //LAMBDA EXPRESSION
        divisionChooser.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                String s = newValue;
                int i = getKeyByValue(GeoHelper.DIVLIST, s);
                actualDivID.setText(Integer.toString(i));
            }
        });
    }

    /**
     * This method fills the division chooser based on the country chooser.
     */
    @FXML
    private void handleCountryChooser() {
        divisionChooser.getItems().clear();
        String country = countryChooser.getValue();
        if (country != null) {
            if (country.equals("US")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.US_DIVISION_1));
            } else if (country.equals("UK")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.UK_DIVISION_2));
            } else if (country.equals("Canada")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.CANADA_DIVISION_3));
            }
        }
    }

    /**
     * This method handles the save button.
     * The method checks for empties first, then validates the input. If there are errors, it displays them in an alert.
     * For example, can't have non-numeric characters in the phone number field.
     * Also requires 10 digits. Not sure if I like that or not, but I wanted to try it.
     * In the future I might have a formatter that adds the dashes for the user.
     * It grabs the relevant values for the query from the text fields and combo boxes.
     * It also grabs the division info as well, as long as thats not null.
     * Then it grabs the current user.
     * Then it sets up the query, and then executes the query. I found that CURRENT_TIMESTAMP was EXTREMELY crucial in making sure the timestamps were correct.
     *
     * @throws SQLException the sql exception
     */
    @FXML
    private void handleSaveButton() throws SQLException {
        String errorMessage = "";

        if (customerNameField.getText().isEmpty()) {
            errorMessage += "Please enter a customer name.\n";
        }

        if (customerAddressField.getText().isEmpty()) {
            errorMessage += "Please enter a customer address.\n";
        }

        if (countryChooser.getSelectionModel().isEmpty()) {
            errorMessage += "Please select a country.\n";
        }

        if (divisionChooser.getSelectionModel().isEmpty()) {
            errorMessage += "Please select a division.\n";
        }

        if (customerPhoneField.getText().isEmpty()) {
            errorMessage += "Please enter a phone number.\n";
        } else if (!customerPhoneField.getText().matches("\\d{10}")) {
            errorMessage += "Phone number must be 10 digits (numbers only).\n";
        }

        if (customerPostalCodeField.getText().isEmpty()) {
            errorMessage += "Please enter a postal code.\n";
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return;
        }

        int id = openID;
        String name = customerNameField.getText();
        String address = customerAddressField.getText();
        String phone = customerPhoneField.getText();
        String postalCode = customerPostalCodeField.getText();
        String divisionName = divisionChooser.getValue();


        if (divisionName != null && !divisionName.isEmpty()) {
            for (Map.Entry<Integer, String> entry : GeoHelper.DIVLIST.entrySet()) {
                if (entry.getValue().equals(divisionName)) {
                    divisionID = entry.getKey();
                    break;
                }
            }
        }


        String user = User.getCurrentUser();

        String query = String.format("INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (%d, '%s', '%s', '%s', '%s', CURRENT_TIMESTAMP, '%s', CURRENT_TIMESTAMP, '%s', %d)",
                id,
                name,
                address,
                postalCode,
                phone,
                user,
                user,
                divisionID);


        int rowsAffected = DatabaseHelper.executeUpdate(query);

        if (rowsAffected > 0) {
            System.out.println(rowsAffected + " row(s) inserted successfully.");
        } else {
            System.out.println("Error inserting row.");
        }


        // close the stage after the customer is saved
        ((Stage) saveButton.getScene().getWindow()).close();

    }

    /**
     * A back button to take you back to the customer page.
     *
     * @param event the event
     */
    @FXML
    private void handleBackButton(MouseEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }


}
