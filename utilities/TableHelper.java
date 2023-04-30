package utilities;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import model.Appointment;
import model.Customer;

/**
 * The TableHelper class mostly deals with selection of rows and items from the database.
 */
public class TableHelper {
    /**
     * This one is specific to the Appointment tableview. It returns the selected appointment.
     * I handled the null case in the method call itself, so there was no need to implement a null check here.
     *
     * @param tableView the table view
     * @return the appointment
     */
    public static Appointment selectedApptRow(TableView<Appointment> tableView) {
        // Get the selected appointment
        Appointment row = tableView.getSelectionModel().getSelectedItem();
        // Appointment selected, return it
        return row;
    }

    /**
     * This method is specific to the Customer tableview. It returns the selected customer.
     * I handled the null case in the method call itself, so there was no need to implement a null check here.
     *
     * @param tableView the table view
     * @return the customer
     */
    public static Customer selectedCustomerRow(TableView<Customer> tableView) {
        // Get the selected appointment
        Customer row = tableView.getSelectionModel().getSelectedItem();

        return row;
    }


    /**
     * This method obtains the ID of the highlighted appointment.
     * This was mainly used in the deletion methods.
     * It gets the selected row, looks for the first item in the row, which happens to be ID for all tableviews.
     * It then returns the ID.
     *
     * @param tableView the table view
     * @return the integer Appointment Id
     */
    public static Integer highlightedAppointmentID(TableView<Appointment> tableView) {
        // Get the selected row
        ObservableList<Appointment> selectedRow = tableView.getSelectionModel().getSelectedItems();

        // Return null if there is no selected row
        if (selectedRow.isEmpty()) {
            return null;
        }

        // Get the first item in the selected rows list
        Appointment selectedID = selectedRow.get(0);

        // Get the first value of the selected appointment
        return selectedID.getAppointmentID();
    }

    /**
     * This method obtains the ID of the highlighted customer.
     * This was mainly used in the deletion methods.
     * It gets the selected row, looks for the first item in the row, which happens to be ID for all tableviews.
     * It then returns the ID.
     *
     * @param tableView the table view
     * @return the integer customer ID
     */
    public static Integer highlightedCustomerID(TableView<Customer> tableView) {
        // Get the selected row
        ObservableList<Customer> selectedRow = tableView.getSelectionModel().getSelectedItems();

        // Return null if there is no selected row
        if (selectedRow.isEmpty()) {
            return null;
        }

        // Get the first item in the selected rows list
        Customer selectedID = selectedRow.get(0);

        // Get the first value of the selected appointment
        return selectedID.getCustomerID();
    }


}
