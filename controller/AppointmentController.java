package controller;

import dao.DB_Appointments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import utilities.DatabaseHelper;
import utilities.DialogHelper;
import utilities.TableHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Appointment controller class handles the Appointment tab.
 * There are different radio buttons to switch between the default view, month view, and week view.
 * The default view shows all appointments.
 *
 */
public class AppointmentController implements Initializable {


    /**
     * The Appointment table.
     */
    @FXML
    public TableView<Appointment> appointmentTable;
    /**
     * The Default view radio button.
     */
    @FXML
    private RadioButton defaultViewRadioButton;
    /**
     * The Month view radio button.
     */
    @FXML
    private RadioButton monthViewRadioButton;
    /**
     * The Week view radio button.
     */
    @FXML
    private RadioButton weekViewRadioButton;
    /**
     * The Appointment view.
     */
    @FXML
    private ToggleGroup appointmentView;


    /**
     * This checks to see if there are any appointments within the next 15 minutes.
     * The query is hardcoded, but its easily changeable to any time frame.
     * Any appointment is appended to the dialog box and displayed to the user.
     * If the query returns no results, then the user is greeted with a welcome message.
     * I kind of regret that you don't get a nice welcome message if there are appointments, kind of makes it seem like it promotes a toxic work environment.
     * But it's fine, I'm sure the user will be fine.
     */
    public static void appointmentCheck() {
        ResultSet rs = null;
        String query = "SELECT Appointment_ID, Type, Start, End FROM client_schedule.appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 15 MINUTE)";
        rs = DatabaseHelper.executeQuery(query);

        try {
            StringBuilder sb = new StringBuilder("Upcoming Appointments:\n");

            // Check if there are any appointments
            if (!rs.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("WELCOME");
                alert.setHeaderText("There are no upcoming appointments!");
                alert.setContentText("Have a wonderful day!");
                alert.showAndWait();
                return;
            }

            // Iterate through the result set and append each appointment to the StringBuilder
            do {
                int appointmentID = rs.getInt("Appointment_ID");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");

                sb.append("Appointment ID: ").append(appointmentID)
                        .append("\nType: ").append(type)
                        .append("\nStart Time: ").append(start.toString())
                        .append("\nEnd Time: ").append(end.toString())
                        .append("\n\n");
            } while (rs.next());

            // Show an alert with the upcoming appointments
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("WELCOME");
            alert.setHeaderText("There are upcoming appointments!");
            alert.setContentText(sb.toString());
            alert.showAndWait();

        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }


    /**
     * The initialize method makes sure that the radio buttons are exclusive of each other, meaning you can only select one.
     * Weirdly enough, this doesn't work in the fxml due to the JavaFX constraints of the version I'm using.
     * Anyway, then the table columns are set up and the table is populated with all appointments.
     *
     *
     * @param location  the location
     * @param resources the resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appointmentView = new ToggleGroup();
        defaultViewRadioButton.setToggleGroup(appointmentView);
        weekViewRadioButton.setToggleGroup(appointmentView);
        monthViewRadioButton.setToggleGroup(appointmentView);

        setAppointmentTableColumns();
        appointmentTable.setItems(DB_Appointments.loadAppointments("all"));

        appointmentCheck();


    }


    /**
     * Default view. This is the default view, which shows all appointments.
     *
     * @param event the event
     */
    @FXML
    private void clickedDefaultView(ActionEvent event) {
        appointmentTable.setItems(DB_Appointments.loadAppointments("all"));
    }

    /**
     * Monthly view - shows all appointments in the next 30 days.
     *
     * @param event the event
     */
    @FXML
    private void clickedMonthView(ActionEvent event) {
        appointmentTable.setItems(DB_Appointments.loadAppointments("30"));
    }


    /**
     * Weekly view - shows all appointments in the next seven days.
     *
     * @param event the event
     */
    @FXML
    private void clickedWeekView(ActionEvent event) {
        appointmentTable.setItems(DB_Appointments.loadAppointments("7"));
    }

    /**
     * This method handles the action of clicking on Delete.
     * It first checks to see if a row is selected, and if not, it displays an error message.
     * If a row is selected, it gets the appointment ID of the selected row and then deletes the appointment from the database.
     * Then it refreshes the table.
     * There was a bit involved in this to make sure I grabbed the information, then confirmed it with the user SHOWING THE INFORMATION, and then deleting it.
     * Then there's a confirmation also displaying the Appointment information that was deleted.
     *
     * @param event the event
     * @throws SQLException the sql exception
     */
    @FXML
    private void deleteAppointment(MouseEvent event) throws SQLException {
        Appointment selectedRow = TableHelper.selectedApptRow(appointmentTable);
        if (selectedRow != null) {
            int k = TableHelper.highlightedAppointmentID(appointmentTable);
            List<Appointment> appointments = new ArrayList<>();
            ResultSet resultSet = DatabaseHelper.executeQuery("SELECT * FROM appointments WHERE Appointment_ID = " + k);
            String type = null;
            while (resultSet.next()) {
                int appointmentID = resultSet.getInt("Appointment_ID");
                type = resultSet.getString("Type");
                Timestamp start = resultSet.getTimestamp("Start");
                Appointment appointment = new Appointment(appointmentID, null, null, null, type, start, null, null, null, null, null, 0, 0, 0);
                appointments.add(appointment);
            }
            Appointment appointment = appointments.get(0);
            String check = "Appointment ID: " + k + "\nAppointment Type: " + type + "\nAppointment Start: " + appointment.getAppointmentStart();
            if (DialogHelper.popUpDeleteAppointments("WAIT", "Are you sure you want\n to delete this appointment?\n\n" + check + "\n\n\n")) {
                String deleteQuery = "DELETE FROM appointments WHERE Appointment_ID = " + k;
                int rowsAffected = DatabaseHelper.executeUpdate(deleteQuery);
                if (rowsAffected > 0) {
                    DialogHelper.popUpSuccess("Appointment Deleted", check + "\n has been successfully deleted.");
                    appointmentTable.setItems(DB_Appointments.loadAppointments("all"));
                } else {
                    DialogHelper.popUpError("ERROR", "Appointment \n\n " + check + "\n\n was not deleted.");
                }
            }
        } else {
            utilities.DialogHelper.popUpError("ERROR", "You must select an appointment to proceed!");
        }
    }


    /**
     * The Edit button that passes the selected appointment to the EditAppointmentController
     * NOTE: THERE IS A LAMBDA EXPRESSION HERE!
     * stage.setOnHidden(e -> { //LAMBDA EXPRESSION
     *
     * @param event the event
     * @throws IOException the io exception
     * @see javafx.stage.WindowEvent
     * This method is the LAMBDA that takes a parameter 'e' of type WindowEvent and calls methods
     * to refresh the table when the scene closes.
     */
    @FXML
    void editAppointment(MouseEvent event) throws IOException {
        Appointment selectedItem = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditAppointment.fxml"));
            Parent root = loader.load();
            EditAppointmentController roller = loader.getController();
            roller.setAppointment(selectedItem);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.setOnHidden(e -> { //LAMBDA EXPRESSION
                appointmentTable.setItems(DB_Appointments.loadAppointments("all"));
                setAppointmentTableColumns();
            });
            stage.showAndWait();
        } else {
            DialogHelper.popUpError("ERROR", "You must select an appointment to proceed!");
        }
    }

    /**
     * The Add button that opens up the AddAppointment screen.
     * NOTE: THERE IS A LAMBDA EXPRESSION HERE!
     *  stage.setOnHidden(e -> { //LAMBDA EXPRESSION
     *  This takes a parameter 'e' of type WINDOWEVENT and calls methods to refresh the customer table.
     *  This is executed when the Edit Appointment screen is closed.
     *
     * @param event the event
     * @throws IOException the io exception
     * @see javafx.stage.WindowEvent
     * This [LAMBDA EXPRESSION] method takes a parameter 'e' of type WindowEvent.
     * It calls methods to refresh the tableview once the scene is closed.
     * for more info about the Lambda expression.
     */
    @FXML
    private void addAppointment(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddAppointment.fxml"));
        Parent root = loader.load();
        AddAppointmentController controller = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setOnHidden(e -> { //LAMBDA EXPRESSION
            appointmentTable.setItems(DB_Appointments.loadAppointments("all"));
            setAppointmentTableColumns();
        });
        stage.showAndWait();
    }


    /**
     * This method sets the columns for the appointment table.
     * PLEASE NOTE THAT I HID SOME COLUMNS FROM VIEW ACCORDING TO REQUIREMENT 3B
     */
    private void setAppointmentTableColumns() {
        // Add columns to the table view
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("Appointment ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));

        TableColumn<Appointment, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));

        TableColumn<Appointment, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));

        TableColumn<Appointment, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));

        TableColumn<Appointment, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));

        TableColumn<Appointment, Timestamp> startColumn = new TableColumn<>("Start");
        startColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));

        TableColumn<Appointment, Timestamp> endColumn = new TableColumn<>("End");
        endColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));

        TableColumn<Appointment, Timestamp> createDateColumn = new TableColumn<>("Create Date");
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCreateDate"));

        TableColumn<Appointment, String> createdByColumn = new TableColumn<>("Created By");
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCreatedBy"));

        TableColumn<Appointment, Timestamp> lastUpdateColumn = new TableColumn<>("Last Update");
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLastUpdate"));

        TableColumn<Appointment, String> lastUpdatedByColumn = new TableColumn<>("Last Updated By");
        lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentLastUpdatedBy"));

        TableColumn<Appointment, Integer> customerIdColumn = new TableColumn<>("Customer ID");
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));

        TableColumn<Appointment, Integer> userIdColumn = new TableColumn<>("User ID");
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentUserID"));

        TableColumn<Appointment, Integer> contactIdColumn = new TableColumn<>("Contact ID");
        contactIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentContactID"));


        appointmentTable.getColumns().setAll(idColumn, titleColumn, descriptionColumn, locationColumn, typeColumn, startColumn, endColumn, createDateColumn, createdByColumn, lastUpdateColumn, lastUpdatedByColumn, customerIdColumn, userIdColumn, contactIdColumn);



        //deliberately hid these specific columns from view according to requirement 3B
        createDateColumn.setVisible(false);
        createdByColumn.setVisible(false);
        lastUpdateColumn.setVisible(false);
        lastUpdatedByColumn.setVisible(false);
    }


    /**
     * This button refreshes the appointment table for ease of use.
     *
     * @param event the event
     */
    public void refreshView(MouseEvent event) {
        setAppointmentTableColumns();
        appointmentTable.setItems(DB_Appointments.loadAppointments("all"));
    }
}
