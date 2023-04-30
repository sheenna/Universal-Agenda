package controller;

import dao.DB_Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import utilities.DatabaseHelper;
import utilities.DialogHelper;
import utilities.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Customer controller class handles the Customer tab.
 */
public class CustomerController implements Initializable {

    /**
     * The Customer table that displays all customers and their information.
     */
    @FXML
    public TableView<Customer> customerTable;

    /**
     * The Add customer button.
     */
    @FXML
    private Button addCustomerButton;

    /**
     * The Update customer button.
     */
    @FXML
    private Button updateCustomerButton;

    /**
     * The Delete customer button.
     */
    @FXML
    private Button deleteCustomerButton;


    /**
     * The initialize method sets the customer table columns and populates the table with customer data.
     *
     * @param location  the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerTable.setItems(DB_Customers.loadCustomers());
        setCustomerTableColumns();
    }


    /**
     * The Add Customer method just waits for the user to click on the Add button to send them to the Add Customer screen.
     * NOTE: THERE IS A LAMBDA EXPRESSION HERE!
     * stage.setOnHidden(e -> { //LAMBDA EXPRESSION
     * This takes a parameter 'e' of type WINDOWEVENT and calls methods to refresh the customer table.
     * This is executed when the Add Customer screen is closed.
     *
     * @param event the event
     * @throws IOException the io exception
     * @see javafx.stage.WindowEvent
     * This [LAMBDA EXPRESSION] method takes a parameter 'e' of type WindowEvent.
     * It calls methods to refresh the tableview once the scene is closed.
     * for more info about the Lambda expression.
     */
// other methods for handling customer actions
    @FXML
    private void addCustomer(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddCustomer.fxml"));
        Parent root = loader.load();
        AddCustomerController controller = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setOnHidden(e -> { //LAMBDA EXPRESSION
            customerTable.setItems(DB_Customers.loadCustomers());
            setCustomerTableColumns();
        });
        stage.showAndWait();
    }

    /**
     * The Edit Customer method grabs the selection that the user made in the customer table and sends it to the Edit Customer screen.
     * If there wasn't a selection, it returns a pop-up error message saying they have to choose a customer to edit.
     * NOTE: THERE IS A LAMBDA EXPRESSION HERE!
     * stage.setOnHidden(e -> { //LAMBDA EXPRESSION
     * This takes a parameter 'e' of type WINDOWEVENT and calls methods to refresh the customer table.
     * This is executed when the Edit Customer screen is closed.
     *
     * @param event the event
     * @throws IOException the io exception
     * @see javafx.stage.WindowEvent
     * This [LAMBDA EXPRESSION] method takes a parameter 'e' of type WindowEvent.
     * It calls methods to refresh the tableview once the scene is closed.
     * for more info about the Lambda expression.
     */
    @FXML
    void editCustomer(MouseEvent event) throws IOException {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditCustomer.fxml"));
            Parent root = loader.load();
            EditCustomerController controller = loader.getController();
            controller.setCustomer(selectedCustomer);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setOnHidden(e -> { //LAMBDA EXPRESSION
                customerTable.setItems(DB_Customers.loadCustomers());
                setCustomerTableColumns();
            });
            stage.showAndWait();
        } else {
            DialogHelper.popUpError("ERROR", "You must select a customer to proceed!");
        }
    }


    /**
     * This grabs the selected customer from the customer table and deletes it from the database.
     * It also deletes any appointments associated with that customer IN ACCORDANCE WITH THE REQUIREMENTS.
     * Custom dialog boxes are used to confirm the deletion and to show success or error messages.
     *
     * @param event the event
     */
    @FXML
    public void deleteCustomer(MouseEvent event) {
        // Get the selected row in the customerTable
        Customer row = TableHelper.selectedCustomerRow(customerTable);

        // If a row is selected, show a confirmation dialog and delete the customer if confirmed
        if (row != null) {
            if (DialogHelper.popUpDeleteCustomers()) {
                int k = TableHelper.highlightedCustomerID(customerTable);
                String deleteQuery1 = "DELETE FROM appointments WHERE Customer_ID = " + k;
                String deleteQuery2 = "DELETE FROM customers WHERE Customer_ID = " + k;
                DatabaseHelper.executeUpdate(deleteQuery1);
                int rowsAffected = DatabaseHelper.executeUpdate(deleteQuery2);
                if (rowsAffected > 0) {
                    DialogHelper.popUpSuccess("SUCCESS", "Customer has been deleted.");
                    refreshCustomerTable();
                } else {
                    System.out.println("Error deleting customer.");
                }
            }
        } else {
            // No customer selected, show an error message
            utilities.DialogHelper.popUpError("ERROR", "You must select a customer to proceed!");
        }
    }


    /**
     * This method refreshes the customer table.
     *
     */
    private void refreshCustomerTable() {
        customerTable.getItems().clear();
        String query = "SELECT * FROM customers";
        ResultSet rs = DatabaseHelper.executeQuery(query);
        List<Customer> customers = DB_Customers.loadCustomers();
        ObservableList<Customer> customerList = FXCollections.observableArrayList(customers);
        customerTable.setItems(customerList);
    }


    /**
     * This method sets the columns for the customer table.
     */
    private void setCustomerTableColumns() {
        // Add columns to the table view
        TableColumn<Customer, Integer> idColumn = new TableColumn<>("Customer ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        TableColumn<Customer, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        TableColumn<Customer, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));

        TableColumn<Customer, String> postalCodeColumn = new TableColumn<>("Postal Code");
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));

        TableColumn<Customer, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));

        TableColumn<Customer, Timestamp> createDateColumn = new TableColumn<>("Create Date");
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("customerCreateDate"));

        TableColumn<Customer, String> createdByColumn = new TableColumn<>("Created By");
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("customerCreatedBy"));

        TableColumn<Customer, Timestamp> lastUpdateColumn = new TableColumn<>("Last Update");
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("customerLastUpdate"));

        TableColumn<Customer, String> lastUpdatedByColumn = new TableColumn<>("Last Updated By");
        lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("customerLastUpdatedBy"));

        TableColumn<Customer, Integer> divisionIdColumn = new TableColumn<>("Division ID");
        divisionIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerDivisionID"));


        customerTable.getColumns().setAll(idColumn, nameColumn, addressColumn, postalCodeColumn, phoneColumn, createDateColumn, createdByColumn, lastUpdateColumn, lastUpdatedByColumn, divisionIdColumn);
    }

    /**
     * I added a button that would refresh the tables for ease of use.
     *
     * @param event the event
     */
    public void refreshView(MouseEvent event) {
        customerTable.setItems(DB_Customers.loadCustomers());
        setCustomerTableColumns();
    }
}
