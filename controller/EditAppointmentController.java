package controller;

import dao.DB_Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import utilities.DatabaseHelper;
import utilities.DialogHelper;
import utilities.GeoHelper;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * The type Edit Appointment controller handles the Edit Appointment screen.
 */
public class EditAppointmentController {

    /**
     * The Appointment.
     */
    private Appointment appointment;


    /**
     * The Appt ID field.
     */
    @javafx.fxml.FXML
    private TextField apptIDField;
    /**
     * The Appt title field.
     */
    @javafx.fxml.FXML
    private TextField apptTitleField;
    /**
     * The Appt location field.
     */
    @javafx.fxml.FXML
    private TextField apptLocationField;
    /**
     * The Appt description field.
     */
    @javafx.fxml.FXML
    private TextField apptDescriptionField;
    /**
     * The Save button.
     */
    @javafx.fxml.FXML
    private Button saveButton;
    /**
     * The Back button.
     */
    @javafx.fxml.FXML
    private Button backButton;
    /**
     * The Appt title label.
     */
    @javafx.fxml.FXML
    private Label apptTitleLabel;
    /**
     * The Appt type field.
     */
    @javafx.fxml.FXML
    private TextField apptTypeField;
    /**
     * The Appt date picker.
     */
    @javafx.fxml.FXML
    private DatePicker apptDatePicker;
    /**
     * The Appt start time.
     */
    @javafx.fxml.FXML
    private ComboBox<String> apptStartTime;
    /**
     * The Customer chooser.
     */
    @javafx.fxml.FXML
    private ComboBox<String> customerChooser;
    /**
     * The Appt end time.
     */
    @javafx.fxml.FXML
    private ComboBox<String> apptEndTime;
    /**
     * The Contact chooser.
     */
    @javafx.fxml.FXML
    private ComboBox<String> contactChooser;
    /**
     * The User chooser.
     */
    @javafx.fxml.FXML
    private ComboBox<String> userChooser;

    /**
     * This method checks to see whether the start time and the end time are within business hours.
     * First it notes the Eastern Time Zone, then it notes the local time zone.
     * It takes the local times and converts them to a ZonedDateTime in the local time zone.
     * Then it takes those times and grabs the time in the Eastern Time Zone.
     * It then checks to see if the start time and end time are within business hours.
     *
     * @param localStart the local start
     * @param localEnd   the local end
     * @return the boolean
     */
    public static boolean duringBusinessHours(LocalDateTime localStart, LocalDateTime localEnd) {
        ZoneId businessZone = ZoneId.of("America/New_York");
        ZoneId localZone = ZoneId.systemDefault();

        ZonedDateTime localStartZDT = ZonedDateTime.of(localStart, localZone);
        ZonedDateTime businessStartZDT = localStartZDT.withZoneSameInstant(businessZone);

        ZonedDateTime localEndZDT = ZonedDateTime.of(localEnd, localZone);
        ZonedDateTime businessEndZDT = localEndZDT.withZoneSameInstant(businessZone);

        // Get the opening and closing times in the user's time zone
        LocalTime userOpeningTime = LocalTime.of(8, 0); // replace with actual opening time
        LocalTime userClosingTime = LocalTime.of(22, 0); // replace with actual closing time

        // Check if localStart and localEnd fall on or within business hours in Eastern Time Zone
        return (businessStartZDT.toLocalTime().equals(userOpeningTime) || businessStartZDT.toLocalTime().isAfter(userOpeningTime))
                && (businessEndZDT.toLocalTime().equals(userClosingTime) || businessEndZDT.toLocalTime().isBefore(userClosingTime));
    }


    /**
     * This function checks the start and end times to see if it overlaps with any other appointment times.
     * It does this by checking the start and end times of each appointment in the database against the new start and end times.
     * I did this in Timestamp here.
     *
     * @param start         the start
     * @param end           the end
     * @param appointmentID the appointment ID
     * @return the boolean
     */
    public static boolean checkForAppointmentOverlap(Timestamp start, Timestamp end, int appointmentID) {
        ObservableList<Appointment> appointments = DB_Appointments.loadAppointments("all");

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID() != appointmentID) { // exclude the current appointment ID
                Timestamp start1 = appointment.getAppointmentStart();
                Timestamp end1 = appointment.getAppointmentEnd();

                // Check if the new appointment overlaps with any existing appointment
                if ((start1.before(end) && end1.after(start)) ||
                        (start.before(end1) && end.after(start1))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This function simply checks the combobox values to see if they're a String with the Name + ID
     * or if they're just the ID.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isFirstCharacterNumber(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        char firstChar = s.charAt(0);
        return Character.isDigit(firstChar);
    }


    /**
     * This method grabs the Appointment that was selected and passes it to the controller.
     * It also sets the Appointment object to the appointment.
     * It sets up all the fields to load the relevant information.
     *
     * @param appointment the appointment
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;

        int appointmentID = appointment.getAppointmentID();
        apptIDField.setText(String.valueOf(appointmentID));
        apptTitleField.setText(appointment.getAppointmentTitle());
        apptDescriptionField.setText(appointment.getAppointmentDescription());
        apptLocationField.setText(appointment.getAppointmentLocation());
        apptTypeField.setText(appointment.getAppointmentType());
        int apptCustomerID = appointment.getAppointmentCustomerID();
        int apptContactID = appointment.getAppointmentContactID();
        int apptUserID = appointment.getAppointmentUserID();

        // Set the appointment start date and time
        LocalDateTime start = (appointment.getAppointmentStart()).toLocalDateTime();
        LocalDate startDate = start.toLocalDate();
        LocalTime startTime = start.toLocalTime();
        apptDatePicker.setValue(startDate);
        apptStartTime.setItems(FXCollections.observableArrayList(GeoHelper.TIME_LIST));
        apptStartTime.setValue(startTime.format(DateTimeFormatter.ofPattern("hh:mm a")));

        // Set the appointment end date and time
        apptEndTime.setItems(FXCollections.observableArrayList(GeoHelper.TIME_LIST));
        LocalDateTime end = appointment.getAppointmentEnd().toLocalDateTime();
        LocalTime endTime = end.toLocalTime();
        apptEndTime.setValue(endTime.format(DateTimeFormatter.ofPattern("hh:mm a")));

        // Set the customer, contact, and user choosers
        customerChooser.setValue(String.valueOf(apptCustomerID));
        contactChooser.setValue(String.valueOf(apptContactID));
        userChooser.setValue(String.valueOf(apptUserID));

    }

    /**
     * Check for empties boolean.
     * This also uses the cute error message appending mechanism that the EditCustomer class uses.
     *
     * @return the boolean
     */
    private boolean checkForEmpties() {
        String errorMessage = "";

        if (apptTitleField.getText().isEmpty() ||
                apptDescriptionField.getText().isEmpty() ||
                apptLocationField.getText().isEmpty() ||
                apptTypeField.getText().isEmpty() ||
                customerChooser.getValue() == null ||
                contactChooser.getValue() == null ||
                userChooser.getValue() == null ||
                apptStartTime.getValue() == null ||
                apptEndTime.getValue() == null) {
            errorMessage += "All fields are required.\n";

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * This method does the other error checks necessary to validate the appointment using the LocalDateTime versions of the start and end times that the customer chose.
     * Checks for business hours, start time in the past, end time before start time, and appointment duration.
     *
     * @param st the st
     * @param e  the e
     * @return the boolean
     */
    private boolean validateAppointment(LocalDateTime st, LocalDateTime e) {
        String errorMessage = "";

        // Check if appointment is outside business hours

        if (!duringBusinessHours(st, e)) {
            errorMessage += "Appointment is outside business hours.\n";
        }

        if (st.isBefore(LocalDateTime.now())) {
            errorMessage += "Start time is in the past!\n";
        }

        if (e.isBefore(st)) {
            errorMessage += "You must choose a start time that is before your end time.\n";
        }

        Duration maxDuration = Duration.ofHours(3);
        Duration appointmentDuration = Duration.between(st, e);
        if (appointmentDuration.compareTo(maxDuration) > 0) {
            errorMessage += "Appointment duration cannot exceed 3 hours.\n";
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }

        return true;
    }

    /**
     * This method handles the Save Button actions
     * 1. Runs them through some of the error checks, like checking for overlaps and empties.
     * 2. If there are no errors, then it converts everything to the correct types.
     * 3. Then it makes sure that the user actually made a change.
     * 4. If they did, then it updates the appointment in the database and returns to the main screen.
     *
     * @param event the event
     */
    @FXML
    private void handleSaveButton(MouseEvent event) {
        if (checkForAppointmentOverlap(appointment.getAppointmentStart(), appointment.getAppointmentEnd(), appointment.getAppointmentID())) {
            String errorMessage = "Appointment overlaps with existing appointment.\n";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }
        if (checkForEmpties()) {
            LocalDate selectedDate = apptDatePicker.getValue();
            LocalTime selectedStartTime = LocalTime.parse(apptStartTime.getValue(), DateTimeFormatter.ofPattern("hh:mm a"));
            LocalDateTime localStart = LocalDateTime.of(selectedDate, selectedStartTime);

            ZoneId localZone = ZoneId.systemDefault();
            ZonedDateTime zdtLocalStart = ZonedDateTime.of(localStart, localZone);
            ZonedDateTime utcZonedStart = zdtLocalStart.withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp start = Timestamp.valueOf(utcZonedStart.toLocalDateTime());


            LocalTime selectedEndTime = LocalTime.parse(apptEndTime.getValue(), DateTimeFormatter.ofPattern("hh:mm a"));
            LocalDateTime localEnd = LocalDateTime.of(selectedDate, selectedEndTime);

            ZonedDateTime zdtLocalEnd = ZonedDateTime.of(localEnd, localZone);
            ZonedDateTime utcZonedEnd = zdtLocalEnd.withZoneSameInstant(ZoneId.of("UTC"));
            Timestamp end = Timestamp.valueOf(utcZonedEnd.toLocalDateTime());

            String s = apptIDField.getText();
            int appointmentID = Integer.parseInt(s);
            if (validateAppointment(localStart, localEnd)) {
                int customerID;
                int contactID;
                int userID;

                if (isFirstCharacterNumber(customerChooser.getValue())) {
                    customerID = Integer.parseInt(customerChooser.getValue());
                } else {
                    customerID = DatabaseHelper.identifyIDs(customerChooser.getValue());
                }
                if (isFirstCharacterNumber(contactChooser.getValue())) {
                    contactID = Integer.parseInt(contactChooser.getValue());
                } else {
                    contactID = DatabaseHelper.identifyIDs(contactChooser.getValue());
                }
                if (isFirstCharacterNumber(userChooser.getValue())) {
                    userID = Integer.parseInt(userChooser.getValue());
                } else {
                    userID = DatabaseHelper.identifyIDs(userChooser.getValue());
                }

                String user = User.getCurrentUser();


                String title = apptTitleField.getText();
                String description = apptDescriptionField.getText();
                String location = apptLocationField.getText();
                String type = apptTypeField.getText();


                // Check for changes
                if (appointment != null && appointment.getAppointmentID() == appointmentID
                        && appointment.getAppointmentTitle().equals(apptTitleField.getText())
                        && appointment.getAppointmentDescription().equals(apptDescriptionField.getText())
                        && appointment.getAppointmentLocation().equals(apptLocationField.getText())
                        && appointment.getAppointmentType().equals(apptTypeField.getText())
                        && appointment.getAppointmentStart().equals(start)
                        && appointment.getAppointmentEnd().equals(end)
                        && appointment.getAppointmentCustomerID() == Integer.parseInt(customerChooser.getValue())
                        && appointment.getAppointmentContactID() == Integer.parseInt(contactChooser.getValue())
                        && appointment.getAppointmentUserID() == Integer.parseInt(userChooser.getValue())) {
                    // No changes, close the stage
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                    return;
                }

                String query = String.format("UPDATE appointments SET Title='%s', Description='%s', Location='%s', Type='%s', Start='%s', End='%s', Last_Update=CURRENT_TIMESTAMP, Last_Updated_By='%s', Customer_ID=%d, User_ID=%d, Contact_ID=%d WHERE Appointment_ID=%d",
                        title,
                        description,
                        location,
                        type,
                        start,
                        end,
                        user,
                        customerID,
                        userID,
                        contactID,
                        appointmentID
                );

                int rowsAffected = DatabaseHelper.executeUpdate(query);

                if (rowsAffected > 0) {
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();
                } else {
                    DialogHelper.popUpError("ERROR", "Failed to update appointment!");
                }

                // close the stage after the customer is saved
                ((Stage) saveButton.getScene().getWindow()).close();
            }
        }
    }


    /**
     * The Back button for taking you back to the main screen.
     *
     * @param event the event
     */
    @FXML
    public void handleBackButton(MouseEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    /**
     * This method fills the Customer chooser ComboBox with names and IDs of the customers.
     *
     * @param event the event
     */
    public void fillCustomerChooser(MouseEvent event) {
        ObservableList<String> forCustomerBox = DatabaseHelper.getNamesAndIDs("customers", "Customer_Name", "Customer_ID");
        customerChooser.setItems(forCustomerBox);
    }

    /**
     * This method fills the Contact chooser ComboBox with names and IDs of the contacts.
     *
     * @param event the event
     */
    public void fillContactChooser(MouseEvent event) {
        ObservableList<String> forContactBox = DatabaseHelper.getNamesAndIDs("contacts", "Contact_Name", "Contact_ID");
        contactChooser.setItems(forContactBox);
    }

    /**
     * This method fills the User chooser ComboBox with names and IDs of the users.
     *
     * @param event the event
     */
    public void filluserChooser(MouseEvent event) {
        ObservableList<String> forUserBox = DatabaseHelper.getNamesAndIDs("users", "User_Name", "User_ID");
        userChooser.setItems(forUserBox);
    }

    /**
     * This method fills the Start Time ComboBox with the times to be chosen.
     *
     * @param event the event
     */
    public void fillTimes(MouseEvent event) {
        final ObservableList<String> TIME_LIST = FXCollections.observableArrayList(GeoHelper.TIME_LIST);
        apptStartTime.setItems(TIME_LIST);
    }
}
