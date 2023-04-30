package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * The DialogHelper class handles a majority of the pop-up windows for the program.
 * A majority of the alerts needed specific unique messages, so having a few methods to handle them was easier.
 * This was also a good way to keep the code clean and easy to read.
 */
public class DialogHelper {

    /**
     * Used for any success alerts.
     * Takes in custom header and content texts as arguments and provides a simple alert.
     *
     * @param h the header string
     * @param c the content string
     */
    public static void popUpSuccess(String h, String c) {
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setResizable(true);
        success.getDialogPane().setPrefSize(350, 190);
        success.setTitle("  ");
        success.setHeaderText(h);
        success.setContentText(c);
        success.showAndWait();
    }

    /**
     * Used for any Error alerts.
     * Takes in custom header and content texts as arguments and provides a simple alert.
     *
     * @param h the header string
     * @param c the content string
     */
    public static void popUpError(String h, String c) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setResizable(true);
        error.getDialogPane().setPrefSize(350, 190);
        error.setTitle("  ");
        error.setHeaderText(h);
        error.setContentText(c);
        error.showAndWait();
    }

    /**
     * This alert is used when the user attempts to delete an appointment.
     * Specifically, the major difference between this one and the previous two are the Confirm and Cancel buttons.
     *
     * @param h the h
     * @param c the c
     * @return the boolean
     */
    public static boolean popUpDeleteAppointments(String h, String c) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setResizable(true);
        confirmation.getDialogPane().setPrefSize(350, 300);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(h);
        confirmation.setContentText(c);

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(confirmButton, cancelButton);

        // Wait for user response
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
    }

    /**
     * This alert is used when the user attempts to delete a customer.
     * This also has the customer and cancel buttons, but the content text specifies that all associated appointments will be deleted.
     * This is to follow REQUIREMENT A #2
     *
     * @return the boolean
     */
    public static boolean popUpDeleteCustomers() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setResizable(true);
        confirmation.getDialogPane().setMinSize(350, 220);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("WAIT");
        confirmation.setContentText("Are you sure you want to delete this customer?\n" +
                "All associated appointments will be deleted!");

        // Add buttons
        ButtonType confirmButton = new ButtonType("Confirm");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(confirmButton, cancelButton);

        // Wait for user response
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == confirmButton;
    }
}
