package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import utilities.DialogHelper;
import utilities.ReportHelper;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Reports controller class handles the Reports tab.
 */
public class ReportsController implements Initializable {

    /**
     * The Get report button.
     */
    @FXML
    private Button getReportButton;
    /**
     * The Reports chooser.
     */
    @FXML
    private ComboBox<String> reportsChooser;

    /**
     * The Report description.
     */
    @FXML
    private Label reportDescription;

    /**
     * The Reports text area.
     */
    @FXML
    private TextArea reportsTextArea;


    /**
     * Initialize.
     *
     * @param location  the location
     * @param resources the resources
     * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
     * It's been incredibly useful to use the LAMBDA EXPRESSIONS for addListeners, especially considering without them, the code would be a lot messier and more difficult to read.
     * They're used for anonymous expressions without having to define a whole Class for them, and they're
     * especially useful in conjunction with Java's built-in methods.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        List<String> reports = Arrays.asList("Monthly Appointment Summaries", "Contact Schedules", "Customer Geography");
        reportsChooser.getItems().addAll(reports);


        //LAMBDA EXPRESSION
        reportsChooser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Monthly Appointment Summaries")) {
                reportDescription.setText("A report displaying the total number of customer appointments by type and month. \n\nClick \"Get Report\" to generate this report.");
            } else if (newValue.equals("Contact Schedules")) {
                reportDescription.setText("A schedule for each contact in your organization that includes appointment ID, title, type, description, start date and time, end date and time and customer ID. \n\nClick \"Get Report\" to generate this report.");
            } else if (newValue.equals("Customer Geography")) {
                reportDescription.setText("A report that shows the number of customers in each country, further broken down by number of customers in each division. Divisions with no customers are not displayed. \n\nClick \"Get Report\" to generate this report.");
            }
        });
    }

    /**
     * Gets report.
     *
     * @param event the event
     * @throws SQLException the sql exception
     */
    @FXML
    private void getReport(MouseEvent event) throws SQLException {
        String selectedReport = reportsChooser.getValue();
        if (selectedReport == null || selectedReport.isEmpty()) {
            DialogHelper.popUpError("WAIT", "Please select a report to proceed!");
        }
        String reportText = ReportHelper.generateReport(selectedReport);
        reportsTextArea.setText(reportText);
    }


}
