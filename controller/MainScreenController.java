package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * The Main screen controller class handles the main screen - mostly the management of the tabPane.
 * It loads the tabs and points the way towards the .fxml files for each tab.
 */
public class MainScreenController {

    /**
     * The Appointments tab.
     */
    @FXML
    public Tab appointmentsTab;
    /**
     * ACR stands for Appointment, Customer, Reports. It's the tabPane that holds the three tabs.
     * I had a lot of trouble with this in particular, mostly because I had a hard time understanding the way the individual tabPanes worked together.
     * I had to do a lot of trial and error to get this to work.
     * But I was also dead set on using tabs, as it made the most visual sense to me.
     */
    @FXML
    private TabPane acrTabPane;
    /**
     * The Customers tab.
     */
    @FXML
    private Tab customersTab;

    /**
     * The Reports tab.
     */
    @FXML
    private Tab reportsTab;

    /**
     * Initializes the MainScreenController class, but mostly just uses the loadTab to direct the way to the .fxml files for each tab.
     */
    @FXML
    public void initialize() {
        loadTab("/view/Appointment.fxml", appointmentsTab);
        loadTab("/view/Customer.fxml", customersTab);
        loadTab("/view/Reports.fxml", reportsTab);
    }


    /**
     * This method is specifically responsible for loading each tab upon initialization.
     *
     * @param resourcePath the resource path
     * @param targetTab    the target tab
     */
    private void loadTab(String resourcePath, Tab targetTab) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Parent content = loader.load();
            targetTab.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
