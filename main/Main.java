//Main.java

package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.DatabaseHelper;

/**
 * *
 *     SHEENNA KOHOUTEK-LEE
 *     STUDENT ID: 000903887
 *
 *     This program is a simple scheduling program called "Scheduler" that allows users to create and manage appointments and customers. It also produces a few simple reports.
 *
 *  */



/**
 * The Main Class
 * This class is the starting point of the program.
 */
public class Main extends Application {

    /**
     * This is the main method of the program.
     * It loads the login screen and establishes a connection with the database.
     * It also closes the connection when the program is closed.
     * The System.setProperty line is to disable the alerts regarding the "discrepancy" between the JavaFX version and the Java version.
     * @param args arguments
     */
    public static void main(String[] args) {
        System.setProperty("javafx.pseudoClassOverrideEnabled", "false");
        DatabaseHelper.openConnection();
        launch(args);
        DatabaseHelper.closeConnection();
    }

    /**
     * This method loads the login screen.
     * @param stage the initial log-in stage
     * @throws Exception for runtime
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setScene(new Scene(root, 225, 350));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }


}
