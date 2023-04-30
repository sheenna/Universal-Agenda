package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utilities.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class DB_Appointments handles the database queries to load the appointment tableview.
 * DB stands for... database..
 * This class didn't necessarily need to be in a separate dao package, but initial attempts at the project relied heavily on a dao package, so I kept it.
 */
public class DB_Appointments {

    /**
     * This method loads the appointment tableview by querying the database, loading the results into an Appointment object, and adding that object to an observable list.
     * I used a switch case to determine which query to run based on the "view" parameter.
     * Basically, this is the base mechanism for the View radio buttons that handle the different views of the appointment tableview.
     * The "view" parameter is passed from the controller, and is either "7", "30", or "15".
     * I was originally going to use this to handle the 15 minute appointment alert requirement, but after the implementation, it made more sense to make that separate.
     * I left the code in here because I thought it was a nice way to handle the different views.
     *
     *
     * @param view the view
     * @return the observable list
     */
    public static ObservableList<Appointment> loadAppointments(String view) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Connection conn = DatabaseHelper.connect();
        String query = "SELECT * FROM appointments";

        switch (view) {
            case "7":
                query += " WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)";
                break;
            case "30":
                query += " WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 30 DAY)";
                break;
            case "15":
                query += " WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 15 MINUTE)";
                break;

        }

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        rs.getString("Type"),
                        rs.getTimestamp("Start"),
                        rs.getTimestamp("End"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"),
                        rs.getInt("Contact_ID")
                );

                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

}
