package model;

import java.sql.Timestamp;

/**
 * The Appointment class handles all the Appointment objects for the program.
 */
public class Appointment {
    /**
     * The Appointment ID - primary key for the Appointment table.
     */
    private int appointmentID;
    /**
     * The Appointment title.
     */
    private String appointmentTitle;
    /**
     * The Appointment description.
     */
    private String appointmentDescription;
    /**
     * The Appointment location.
     */
    private String appointmentLocation;
    /**
     * The Appointment type.
     */
    private String appointmentType;
    /**
     * The start time of the Appointment.
     */
    private Timestamp appointmentStart;
    /**
     * The end time of the Appointment.
     */
    private Timestamp appointmentEnd;
    /**
     * The date that the Appointment was created.
     */
    private Timestamp appointmentCreateDate;
    /**
     * The user that created the Appointment.
     */
    private String appointmentCreatedBy;
    /**
     * The date that the Appointment was last updated.
     */
    private Timestamp appointmentLastUpdate;
    /**
     * The user that last updated the Appointment.
     */
    private String appointmentLastUpdatedBy;
    /**
     * The customer ID connected to the Appointment.
     */
    private int appointmentCustomerID;
    /**
     * The user ID connected to the Appointment.
     */
    private int appointmentUserID;
    /**
     * The contact ID connected to the Appointment.
     */
    private int appointmentContactID;


    /**
     * This is the constructor for an Appointment. Takes in all the parameters for an Appointment.
     * Note here that all the time-related parameters are of type Timestamp. In order to get the time conversions on the same level, I forced the conversion.
     * This is not ideal, but I needed all of the time to be in the same format so I could troubleshoot the time conflicts.
     * I ended up leaving them as timestamps because it made the most sense to me.
     *
     * @param appointmentID            the appointment ID
     * @param appointmentTitle         the appointment title
     * @param appointmentDescription   the appointment description
     * @param appointmentLocation      the appointment location
     * @param appointmentType          the appointment type
     * @param appointmentStart         the appointment start
     * @param appointmentEnd           the appointment end
     * @param appointmentCreateDate    the appointment create date
     * @param appointmentCreatedBy     the appointment created by
     * @param appointmentLastUpdate    the appointment last update
     * @param appointmentLastUpdatedBy the appointment last updated by
     * @param appointmentCustomerID    the appointment customer ID
     * @param appointmentUserID        the appointment user ID
     * @param appointmentContactID     the appointment contact ID
     */

    public Appointment(int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation, String appointmentType,
                       Timestamp appointmentStart, Timestamp appointmentEnd, Timestamp appointmentCreateDate, String appointmentCreatedBy, Timestamp appointmentLastUpdate,
                       String appointmentLastUpdatedBy, int appointmentCustomerID, int appointmentUserID, int appointmentContactID) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.appointmentCreateDate = appointmentCreateDate;
        this.appointmentCreatedBy = appointmentCreatedBy;
        this.appointmentLastUpdate = appointmentLastUpdate;
        this.appointmentLastUpdatedBy = appointmentLastUpdatedBy;
        this.appointmentCustomerID = appointmentCustomerID;
        this.appointmentUserID = appointmentUserID;
        this.appointmentContactID = appointmentContactID;
    }

    /**
     * Gets appointment ID.
     *
     * @return the appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Sets appointment ID.
     *
     * @param appointmentID the appointment ID
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }


    /**
     * Gets appointment title.
     *
     * @return the appointment title
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * Sets appointment title.
     *
     * @param appointmentTitle the appointment title
     */
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    /**
     * Gets appointment description.
     *
     * @return the appointment description
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * Sets appointment description.
     *
     * @param appointmentDescription the appointment description
     */
    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    /**
     * Gets appointment location.
     *
     * @return the appointment location
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * Sets appointment location.
     *
     * @param appointmentLocation the appointment location
     */
    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    /**
     * Gets appointment type.
     *
     * @return the appointment type
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * Sets appointment type.
     *
     * @param appointmentType the appointment type
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**
     * Gets appointment start.
     *
     * @return the appointment start
     */
    public Timestamp getAppointmentStart() {
        return appointmentStart;
    }

    /**
     * Sets appointment start.
     *
     * @param appointmentStart the appointment start
     */
    public void setAppointmentStart(Timestamp appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    /**
     * Gets appointment end.
     *
     * @return the appointment end
     */
    public Timestamp getAppointmentEnd() {
        return appointmentEnd;
    }

    /**
     * Sets appointment end.
     *
     * @param appointmentEnd the appointment end
     */
    public void setAppointmentEnd(Timestamp appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }

    /**
     * Gets appointment create date.
     *
     * @return the appointment create date
     */
    public Timestamp getAppointmentCreateDate() {
        return appointmentCreateDate;
    }

    /**
     * Sets appointment create date.
     *
     * @param appointmentCreateDate the appointment create date
     */
    public void setAppointmentCreateDate(Timestamp appointmentCreateDate) {
        this.appointmentCreateDate = appointmentCreateDate;
    }

    /**
     * Gets appointment created by.
     *
     * @return the appointment created by
     */
    public String getAppointmentCreatedBy() {
        return appointmentCreatedBy;
    }

    /**
     * Sets appointment created by.
     *
     * @param appointmentCreatedBy the appointment created by
     */
    public void setAppointmentCreatedBy(String appointmentCreatedBy) {
        this.appointmentCreatedBy = appointmentCreatedBy;
    }

    /**
     * Gets appointment last update.
     *
     * @return the appointment last update
     */
    public Timestamp getAppointmentLastUpdate() {
        return appointmentLastUpdate;
    }

    /**
     * Sets appointment last update.
     *
     * @param appointmentLastUpdate the appointment last update
     */
    public void setAppointmentLastUpdate(Timestamp appointmentLastUpdate) {
        this.appointmentLastUpdate = appointmentLastUpdate;
    }

    /**
     * Gets appointment last updated by.
     *
     * @return the appointment last updated by
     */
    public String getAppointmentLastUpdatedBy() {
        return appointmentLastUpdatedBy;
    }

    /**
     * Sets appointment last updated by.
     *
     * @param appointmentLastUpdatedBy the appointment last updated by
     */
    public void setAppointmentLastUpdatedBy(String appointmentLastUpdatedBy) {
        this.appointmentLastUpdatedBy = appointmentLastUpdatedBy;
    }

    /**
     * Gets appointment customer ID.
     *
     * @return the appointment customer ID
     */
    public int getAppointmentCustomerID() {
        return appointmentCustomerID;
    }

    /**
     * Sets appointment customer ID.
     *
     * @param appointmentCustomerID the appointment customer ID
     */
    public void setAppointmentCustomerID(int appointmentCustomerID) {
        this.appointmentCustomerID = appointmentCustomerID;
    }

    /**
     * Gets appointment user ID.
     *
     * @return the appointment user ID
     */
    public int getAppointmentUserID() {
        return appointmentUserID;
    }

    /**
     * Sets appointment user ID.
     *
     * @param appointmentUserID the appointment user ID
     */
    public void setAppointmentUserID(int appointmentUserID) {
        this.appointmentUserID = appointmentUserID;
    }

    /**
     * Gets appointment contact ID.
     *
     * @return the appointment contact ID
     */
    public int getAppointmentContactID() {
        return appointmentContactID;
    }

    /**
     * Sets appointment contact ID.
     *
     * @param appointmentContactID the appointment contact ID
     */
    public void setAppointmentContactID(int appointmentContactID) {
        this.appointmentContactID = appointmentContactID;
    }


}
