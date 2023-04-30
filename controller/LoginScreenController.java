package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import utilities.DatabaseHelper;
import utilities.DialogHelper;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Login screen controller class handles the login screen.
 */
public class LoginScreenController implements Initializable {

    /**
     * The Username field.
     */
    @FXML
    private TextField usernameField;

    /**
     * The Password field.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * The Username label.
     */
    @FXML
    private Label usernameLabel;

    /**
     * The Password label.
     */
    @FXML
    private Label passwordLabel;

    /**
     * The Time Zone label.
     */
    @FXML
    private Label timeZoneLabel;

    /**
     * The Login button.
     */
    @FXML
    private Button loginButton;

    /**
     * The User time zone.
     */
    @FXML
    private Label userTimeZone;


    /**
     * The resource bundle for the language... The initial identifier it was named seemed to conflict with the loading of the rb, so I changed it to lang.
     */
    private ResourceBundle lang;

    /**
     * The Boolean variable to determine if the user's language is French.
     */
    private boolean isUserLanguageFrench = false;

    /**
     * Initialize method.
     * It grabs the default locale of the user's machine and determines if it is French or English.
     * If it is French, it loads the loadFrench() method.
     * If it isn't, it loads the loadEnglish() method.
     * I was worried that if I set it to "if the default locale is English" as well, that it would break if the user's locale was set to something other than English or French.
     * Not sure if that's a valid concern or not, but I wanted to be safe.
     * There was initially a combo box to allow the user to select their language, but I removed it because one of the additional resources for the project specifically said not to do it.
     * It's not directly on the rubric, but I figured it was better to be safe than sorry.
     *
     * @param url            the url
     * @param resourceBundle the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTimeZone();

        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale.getLanguage().equals(Locale.FRENCH.getLanguage())) {
            isUserLanguageFrench = true;
            loadFrench();
        } else {
            isUserLanguageFrench = false;
            loadEnglish();
        }
    }

    /**
     * This grabs the time zone of the user and displays it in the userTimeZone label. Again, works beautifully. This was one of the first things I did, and it was so smooth.
     */
    private void getTimeZone() {
        //grab user's time zone
        ZoneId zoneID = ZoneId.systemDefault();
        userTimeZone.setText(String.valueOf(zoneID));
    }

    /**
     * This loads the English resource bundle and updates all the labels.
     */
    private void loadEnglish() {
        lang = ResourceBundle.getBundle("resources/login_en");
        updateLoginLabels();
    }

    /**
     * This loads the French resource bundle and updates all the labels.
     * This had to take the Locale as a parameter, because it was throwing an error when I tried to just pass in "fr" as a string.
     * Without it, it kept saying that it couldn't find the resource bundle, OR that it was looking for the English one!
     * Please note that the previous one works FINE and doesn't need the Locale parameter.
     * Please also note that coding can be very frustrating sometimes.
     * <br>
     * Or like, a lot of the time.
     */
    private void loadFrench() {
        lang = ResourceBundle.getBundle("resources/login_fr", new Locale("fr"));
        updateLoginLabels();
    }

    /**
     * Updates all the labels in the program to the specified language.
     * I added in the fields later, but I realized they're technically fields and not labels, but I also didn't want to change the name of the method.
     * I figured the field prompt language change wasn't technically needed, considering the labels right above them say the exact same thing, but I felt it was more consistent.
     *
     */
    private void updateLoginLabels() {
        usernameLabel.setText(lang.getString("username"));
        passwordLabel.setText(lang.getString("password"));
        timeZoneLabel.setText(lang.getString("timezone"));
        loginButton.setText(lang.getString("login"));
        usernameField.setPromptText(lang.getString("username"));
        passwordField.setPromptText(lang.getString("password"));
    }


    /**
     * This handles the login.
     * Stores the input of the username field and password field in variables, throws it on over to the authenticate method as arguments, where it is checked against the database.
     * The error message will show in French IF the user's default locale is French.
     * If the user is authenticated, it will load the main screen.
     * It also passes the user's username and timezone to the User class, as an object, so that it can be used in other methods, like login activity or when updating a customer or appointment.
     * All going well, it loads the main screen, which handles the three major tabs.
     *
     * @param event the event
     * @throws IOException the io exception
     */
    @FXML
    public void loginAttempt(MouseEvent event) throws IOException {
        String u = usernameField.getText();
        String pw = passwordField.getText();

        boolean authenticated = DatabaseHelper.authenticate(u, pw);

        if (!authenticated) {
            if (isUserLanguageFrench) {
                DialogHelper.popUpError("ERREUR", "Nom d'utilisateur et/ou mot de passe incorrect!");
            } else {
                DialogHelper.popUpError("ERROR", "Incorrect username and/or password!");
            }
        } else {
            // Load the main screen
            try {
                ZoneId z = ZoneId.systemDefault();
                User user = new User(u, z);

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(root);

                // Get the stage from the event source
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Set the stage with the new scene
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
