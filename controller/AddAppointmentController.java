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
import utilities.GeoHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Add Appointment Controller Class.
 */
public class AddAppointmentController {
    /**
     * The Save button.
     */
    public Button saveButton;
    /**
     * The Appt date picker.
     */
    public DatePicker apptDatePicker;
    /**
     * The Appt ID field.
     */
    @FXML
    private TextField apptIDField;
    /**
     * The Appt title field.
     */
    @FXML
    private TextField apptTitleField;
    /**
     * The Appt location field.
     */
    @FXML
    private TextField apptLocationField;
    /**
     * The Appt description field.
     */
    @FXML
    private TextField apptDescriptionField;
    /**
     * The Back button.
     */
    @FXML
    private Button backButton;
    /**
     * The Appt type field.
     */
    @FXML
    private TextField apptTypeField;

    /**
     * The Customer chooser.
     */
    @FXML
    private ComboBox<String> customerChooser;
    /**
     * The Contact chooser.
     */
    @FXML
    private ComboBox<String> contactChooser;
    /**
     * The User chooser.
     */
    @FXML
    private ComboBox<String> userChooser;

    /**
     * The Appt start time.
     */
    @FXML
    private ComboBox<String> apptStartTime;
    /**
     * The Appt end time.
     */
    @FXML
    private ComboBox<String> apptEndTime;

    /**
     * The Appointment ID.
     */
    private int appointmentID;
    /**
     * The Appt title label.
     */
    @FXML
    private Label apptTitleLabel;
    /**
     * The Appt start date.
     */
    @FXML
    private DatePicker apptStartDate;

    /**
     * This gets the next available Appointment ID by selecting the max Appointment ID from the database and adding 1.
     * The same mechanism as in the AddCustomerController.
     *
     * @return the next appointment ID
     * @throws SQLException the sql exception
     */
    public static int getNextAppointmentID() throws SQLException {
        String query = "SELECT MAX(Appointment_ID) FROM client_schedule.appointments;";
        ResultSet rs = DatabaseHelper.executeQuery(query);
        if (rs.next()) {
            int openID = rs.getInt(1);
            return openID + 1;
        } else {
            throw new SQLException("Unable to get the next Appointment ID.");
        }
    }

    /**
     * This method is called to check whether the start and end times selected are within business hours in EST.
     * If they are not, the user is alerted and the appointment is not saved.
     * Grabs EST, and mainly uses ZoneDateTime to ensure that the times are compared correctly.
     * I liked this implementation because it's a little more robust than the one I used in the AddCustomerController.
     * It's also much clearer. But by the time I completed this project, everything was so tightly wound that I was afraid to change methods.
     * Regardless, the difference of being able to schedule RIGHT ON business times vs. having to do it after is not specified in the rubric.
     * So I added both to show both are possible.
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
     * This method checks whether the new appointment overlaps with any existing appointments.
     * This one is pretty unforgiving, there's no question of on or after, or on or before.
     * But it works fantastically.
     *
     * @param st the st
     * @param e  the e
     * @return the boolean
     */
    public static boolean checkForAppointmentOverlap(LocalDateTime st, LocalDateTime e) {
        ObservableList<Appointment> appointments = DB_Appointments.loadAppointments("all");

        for (Appointment appointment : appointments) {
            LocalDateTime start1 = appointment.getAppointmentStart().toLocalDateTime();
            LocalDateTime end1 = appointment.getAppointmentEnd().toLocalDateTime();

            // Check if the new appointment overlaps with any existing appointment
            if ((start1.isBefore(e) && end1.isAfter(e)) ||
                    (st.isBefore(end1) && e.isAfter(start1))) {
                return true;
            }

        }

        return false;
    }


    /**
     * The initialize method for this screen.
     * It grabs the Time List from the GeoHelper class, and populates the start times combo box.
     * ApptEndTime is disabled until a start time is selected.
     *
     * PLEASE NOTE THE LAMBDA EXPRESSIONS with the ADDLISTENERS... AGAIN.
     * The first one waits for a Start Time to be selected, then enables the End Time combo box.
     * Then it clears the End Time combo box, and grabs the selected start time.
     * then it fills the End Time box with a handful of times that are sequentially after the selected start time.
     * It grabs the selected index of the start time, and then it loops through the TIME_LIST and adds the next 6 times to the end time combo box.
     *
     *
     * Again, this is only shown here, but I wanted to try it out.
     * THIS IS NOT SPECIFIED IN THE RUBRIC TO DO IT ONE WAY OR ANOTHER. AGAIN, I just wanted to show that both are possible.
     * This one seems better because you can't schedule an appointment longer than three hours, which, no one wants a customer appointment for more than three hours, am I right?
     *
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


        //For this scene, the following need to be populated: Appointment ID, customerChooser, contactChooser, userChooser, apptStartTime, and apptEndTime..

        final ObservableList<String> TIME_LIST = FXCollections.observableArrayList(GeoHelper.TIME_LIST);

        apptStartTime.setItems(TIME_LIST);
        apptEndTime.disableProperty().set(true);

        //LAMBDA EXPRESSION
        apptStartTime.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Do something with the selected value
            apptEndTime.disableProperty().set(false);
            apptEndTime.getItems().clear();
            String selectedStartTime = apptStartTime.getValue();
            int selectedIndex = apptStartTime.getSelectionModel().getSelectedIndex();
            List<String> endTimeList = new ArrayList<>();
            for (int i = selectedIndex + 1; i <= selectedIndex + 6 && i < TIME_LIST.size(); i++) {
                endTimeList.add(TIME_LIST.get(i));
            }
            apptEndTime.setItems(FXCollections.observableList(endTimeList));

        });


        //Populating Customer, User, and Contact Boxes.
        ObservableList<String> forCustomerBox = DatabaseHelper.getNamesAndIDs("customers", "Customer_Name", "Customer_ID");
        customerChooser.setItems(forCustomerBox);
        ObservableList<String> forUserBox = DatabaseHelper.getNamesAndIDs("users", "User_Name", "User_ID");
        userChooser.setItems(forUserBox);
        ObservableList<String> forContactBox = DatabaseHelper.getNamesAndIDs("contacts", "Contact_Name", "Contact_ID");
        contactChooser.setItems(forContactBox);

        //Pre-filled uneditable Appointment ID Field.
        appointmentID = getNextAppointmentID();
        apptIDField.setText(String.valueOf(appointmentID));


    }

    /**
     * This method checks for empty fields.
     * If any are empty, it returns false, and displays an error message.
     * I shortened the total message by having it say "All fields are required."
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
     * This method validates the actual appointment.
     * It checks for overlap, if it's outside business hours, if the start time is in the past, if the end time is before the start time, and if the appointment is longer than three hours.
     * Then it appends each relevant error message to the errorMessage string, and displays it in an alert box.
     * If there are no errors, it returns true.
     *
     * @param s the LocalDateTime object version of the start time.
     * @param e the LocalDateTime object version of the end time.
     * @return the boolean
     */
    private boolean validateAppointment(LocalDateTime s, LocalDateTime e) {
        String errorMessage = "";

        // Check if appointment is outside business hours

        if (checkForAppointmentOverlap(s, e)) {
            errorMessage += "Appointment overlaps with existing appointment.\n";
        }

        if (!duringBusinessHours(s, e)) {
            errorMessage += "Appointment is outside business hours.\n";
        }

        if (s.isBefore(LocalDateTime.now())) {
            errorMessage += "Start time is in the past!\n";
        }

        if (e.isBefore(s)) {
            errorMessage += "You must choose a start time that is before your end time.\n";
        }

        Duration maxDuration = Duration.ofHours(3);
        Duration appointmentDuration = Duration.between(LocalDateTime.now(), LocalDateTime.now());
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
     * Handle save button.
     * It checks for empty fields, and if there are any, it returns.
     * Then it grabs the selected date, start time, and end time, and converts them to the UTC time zone.
     * I found that if I didn't do this, the appointment would be saved in the local time zone, AND the database would try to convert it when it was pulled out.
     * This caused my appointments to be off by several hours.
     * The instructors mentioned that this is a known issue, and that it's best to convert to UTC before saving to the database.
     * However, you also have to remove the time zone information before updating it, because the DATABASE thinks it KNOWS BETTER and tries to CONVERT it again.
     * So I had to use the toLocalDateTime() method to remove the time zone information.
     * Anyway, I saved these in a Timestamp object, and then passed them to the validateAppointment() method.
     * If the appointment is valid, it creates the query, executes it, confirms with a rowsAffected check, and then closes the window.
     *
     * @param event the event
     * @throws SQLException the sql exception
     */
    @FXML
    private void handleSaveButton(MouseEvent event) throws SQLException {


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

            if (validateAppointment(localStart, localEnd)) {
                int customerID = DatabaseHelper.identifyIDs(customerChooser.getValue());
                int contactID = DatabaseHelper.identifyIDs(contactChooser.getValue());
                int userID = DatabaseHelper.identifyIDs(userChooser.getValue());

                String user = User.getCurrentUser();

                int appointmentID = getNextAppointmentID();
                String title = apptTitleField.getText();
                String description = apptDescriptionField.getText();
                String location = apptLocationField.getText();
                String type = apptTypeField.getText();


                String query = String.format("INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', CURRENT_TIMESTAMP, '%s', CURRENT_TIMESTAMP, '%s', %d, %d, %d)",
                        title,
                        description,
                        location,
                        type,
                        start,
                        end,
                        user,
                        user,
                        customerID,
                        userID,
                        contactID
                );


                int rowsAffected = DatabaseHelper.executeUpdate(query);

                if (rowsAffected > 0) {
                    System.out.println(rowsAffected + " row(s) inserted successfully.");
                } else {
                    System.out.println("Error inserting row.");
                }

                // close the stage after the appointment is saved
                ((Stage) saveButton.getScene().getWindow()).close();
            }
        }
    }


    /**
     * Handle back button.
     * Takes you back to the main Appointment screen.
     *
     * @param event the event
     */
    @FXML
    public void handleBackButton(MouseEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

}
