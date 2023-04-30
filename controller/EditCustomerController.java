package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import model.User;
import utilities.DatabaseHelper;
import utilities.DialogHelper;
import utilities.GeoHelper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * The Edit Customer controller Class handles the Edit Customer screen.
 */
public class EditCustomerController {
    /**
     * This label updates whenever the user selects a new locale. It's not fancy, but it may be my favorite part.
     */
    @FXML
    public Label actualDivID;
    /**
     * Instance of the customer being edited.
     */
    private Customer customer;

    /**
     * This is the label that says "Division ID: " right before displaying the actual division ID of the user.
     * At one point, the actualDivID label was broken, so I debated hiding the label and showing it later, but I decided to keep it.
     * Hence, the name "ugh."
     * I kept this so I could remember the pain I felt when I was trying to figure out why the label wasn't working.
     */
    @FXML
    private Label ugh;
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
     * The Division ID. Ends up not really being used.
     */
    private int divisionID;

    /**
     * Essentially retrieves the ID of the division that the user selected.
     * I was being very generic, hoping to use this for other things as well, but it ended up not being as useful as I thought.
     * I kept it in case I wanted to use it for something else.
     *
     *
     * @param map   the map in GeoHelper
     * @param value the value
     * @return the key by value
     */
    public static int getKeyByValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return -1; // Or throw an exception if the value is not found
    }

    /**
     * This method "grabs" the customer that the user selected from the main Customer screen.
     * It then sets any relevant fields or variables to the values of the customer, for  use later in the Class.
     * Had to do a LOT of reverse engineering to figure out how to get the values for the division ID or country or state.
     * The code is a bit messy, and is by NO means clean, but it works, and I'm tired of looking at it.
     *
     * @param customer the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
        int id = customer.getCustomerID();
        String name = customer.getCustomerName();
        String address = customer.getCustomerAddress();
        String phone = customer.getCustomerPhoneNumber();
        String postalCode = customer.getCustomerPostalCode();
        Timestamp createDate = customer.getCustomerCreateDate();
        String createdBy = customer.getCustomerCreatedBy();
        Timestamp lastUpdate = customer.getCustomerLastUpdate();
        String lastUpdatedBy = customer.getCustomerLastUpdatedBy();
        int divisionID = customer.getCustomerDivisionID();


        customerIDField.setText(String.valueOf(id));
        customerNameField.setText(name);
        customerAddressField.setText(address);
        customerPhoneField.setText(phone);
        customerPostalCodeField.setText(postalCode);

        String divisionName = GeoHelper.DIVLIST.get(divisionID);


        String state = null;
        for (Map.Entry<String, List<String>> entry : GeoHelper.COUNTRY_TO_DIVISIONS.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();
            if (value.contains(GeoHelper.DIVLIST.get(divisionID))) {
                state = key;
                break;
            }
        }

        divisionChooser.setValue(divisionName);
        countryChooser.setValue(state);
        actualDivID.setText(Integer.toString(divisionID));

    }

    /**
     * The initialize method. I want to make a note that a majority of the LAMBDA EXPRESSIONS I used in this project are in line with addListeners.
     * It's been incredibly useful to use the LAMBDA EXPRESSIONS for addListeners, especially considering without them, the code would be a lot messier and more difficult to read.
     *
     * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
     * It's been incredibly useful to use the LAMBDA EXPRESSIONS for addListeners, especially considering without them,
     * the code would be a lot messier and more difficult to read. They're used for anonymous expressions without
     * having to define a whole Class for them,
     * and they're especially useful in conjunction with Java's built-in methods.
     * */

    @FXML
    private void initialize() {

        /*Basically, waits for the user to select a new country. ONCE the new country is selected, then a number of things happen.
        * 1. The division chooser combobox empties out.
        * 2. The new selected value is grabbed to determine which list of divisions to load into the division chooser.
        * 3. Also clears the chosen division ID.
        *
        * This essentially guarantees that when the user goes to select a new value, everything is ready for them.  */
        // Set up divisionChooser to change when countryChooser changes
        //LAMBDA EXPRESSION
        countryChooser.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null) {
                divisionChooser.setItems(null);
            } else if (newValue.equals("US")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.US_DIVISION_1));
                divisionChooser.setPromptText("Division");
                actualDivID.setText(" ");

            } else if (newValue.equals("UK")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.UK_DIVISION_2));
                divisionChooser.setPromptText("Division");
                actualDivID.setText(" ");

            } else if (newValue.equals("Canada")) {
                divisionChooser.setItems(FXCollections.observableArrayList(GeoHelper.CANADA_DIVISION_3));
                divisionChooser.setPromptText("Division");
                actualDivID.setText(" ");

            }
        });

        //LAMBDA EXPRESSION

        //Another AddListener LAMBDA to wait for the user to select a new division. Then it grabs the Division ID for the user and sets it to the actualDivID label so they can see it.
        divisionChooser.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                String s = newValue;
                int i = getKeyByValue(GeoHelper.DIVLIST, s);
                actualDivID.setText(Integer.toString(i));
            }
        });

    }


    /**
     * Division click.
     *
     * @param event the event
     */
    public void divisionClick(MouseEvent event) {
    }

    /**
     * Country click.
     *
     * @param event the event
     */
    public void countryClick(MouseEvent event) {
        countryChooser.setItems(FXCollections.observableArrayList(GeoHelper.COUNTRIES));
    }

    /**
     * The Validator method, reused in my both of my Add and Modify screens for Customers and Appointments.
     * I really love this - a very clean, visually appealing way to check for errors.
     * Basically, it starts with an empty string, and does a number of checks for different errors that could occur.
     * Every time it finds an error, it adds it to the string.
     * Once it's done checking, it checks to see if the string is empty. If it is, then it means there were no errors, and the method returns true.
     * If it's not empty, then it means there were errors, and it creates an alert box to display the errors to the user.
     *
     * It's like the code-equivalent of the DMV driving test instructor who stands there with a clipboard and a pen, and just writes down every single thing you do wrong.
     * Not a very cute way of putting it but it's accurate.
     *
     * @return the boolean
     */
    private boolean Validator() {
        String errorMessage = "";

        if (customerNameField.getText().isEmpty()) {
            errorMessage += "Please enter a customer name.\n";
        }

        if (customerAddressField.getText().isEmpty()) {
            errorMessage += "Please enter a customer address.\n";
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
            return true;
        }
        return false;
        //means it passed!
    }

    /**
     * The Save button handler.
     * Once the Save button is clicked, a lot of things happen.
     * 1. Runs everything through the validator. As long as it returns false, it means that all fields have been filled out correctly.
     * (Meaning that all fields were filled, and all fields were filled with the correct data types)
     * 2. Grabs all the data from the text fields and comboboxes.
     * 3. It also checks the data in the fields against the original data to make sure that the user actually made a change, if not, it just closes the window.
     * I didn't want it to update the database if the user didn't make any changes, because the last update time would change, and it would waste resources.
     * But it just closes the window to make the user feel like it did something.
     * 4. Grabs the current user from the User class, so we can track who made the change, and put it in the database.
     * Grabs other necessary info.
     * 5. Then it composes a SQL statement to update the database. I found that using String.format() was the most painless way of doing this.
     * I didn't want to use a bunch of string concatenation, because it's messy and hard to read.
     * 6. It runs the query using DatabaseHelper, confirms whether it worked using the rowsAffected variable, and closes the window.
     *
     * @param event the event
     */
    @FXML
    public void handleSaveButton(MouseEvent event) {
        if (!Validator()) {
            String name = customerNameField.getText();
            String address = customerAddressField.getText();
            String phone = customerPhoneField.getText();
            String postalCode = customerPostalCodeField.getText();
            String divisionName = divisionChooser.getValue();
            String countryName = countryChooser.getValue();

            if (name.equals(customer.getCustomerName())
                    && address.equals(customer.getCustomerAddress())
                    && phone.equals(customer.getCustomerPhoneNumber())
                    && postalCode.equals(customer.getCustomerPostalCode())
                    && divisionName.equals(GeoHelper.DIVLIST.get(customer.getCustomerDivisionID()))) {
                // No changes, close the stage
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
                return;
            }

            String user = User.getCurrentUser();

            // Get the ID of the selected division
            int divisionID = 0;
            for (Map.Entry<Integer, String> entry : GeoHelper.DIVLIST.entrySet()) {
                if (entry.getValue().equals(divisionName)) {
                    divisionID = entry.getKey();
                    break;
                }
            }

            // Build the UPDATE query
            String query = String.format("UPDATE customers SET Customer_Name='%s', Address='%s', Phone='%s', Postal_Code='%s', Division_ID=%d, Last_Update=CURRENT_TIMESTAMP, Last_Updated_By='%s' WHERE Customer_ID=%d",
                    name, address, phone, postalCode, divisionID, user, customer.getCustomerID());

            // Execute the query
            int rowsAffected = DatabaseHelper.executeUpdate(query);
            if (rowsAffected > 0) {
                // Close the stage
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } else {
                DialogHelper.popUpError("ERROR", "Failed to update customer!");
            }
        }
    }


    /**
     * The Back button for taking you back to the main screen.
     *
     * @param event the event
     */
    @FXML
    private void handleBackButton(MouseEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

}
