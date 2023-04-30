package model;

/**
 * The Contact class handles any Contacts objects for the program.
 */
public class Contact {

    /**
     * The Contact ID - primary key for the Contacts table.
     */
    private int contactID;
    /**
     * The Contact name.
     */
    private String contactName;


    /**
     * The Email.
     */
    private String email;

    /**
     * This is the constructor for a new Contact object. It takes in the contact ID, contact name, and email.
     *
     * @param contactID   the contact ID
     * @param contactName the contact name
     * @param email       the email
     */
    public Contact(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * Gets contact ID.
     *
     * @return the contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Sets contact ID.
     *
     * @param contactID the contact ID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact name.
     *
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }


}
