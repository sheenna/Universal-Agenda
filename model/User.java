package model;

import java.time.ZoneId;

/**
 * The User class handles all the User objects for the program.
 */
public class User {
    /**
     * This variable holds the current user's username.
     */
    private static String currentUser;
    /**
     * The constant currentUserZone.
     */
    private static ZoneId currentUserZone;

    /**
     * This is the constructor for a new User object. It takes in the username and userZone.
     *
     * @param username the username
     * @param userZone the user zone
     */
    public User(String username, ZoneId userZone) {
        currentUser = username;
        currentUserZone = userZone;
    }

    /**
     * Gets current user.
     *
     * @return the current user
     */
    public static String getCurrentUser() {
        return currentUser;
    }

}
