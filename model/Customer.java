//Customer.java

package model;

import java.sql.Timestamp;

/**
 * The Customer class handles all the Customer objects for the program.
 */
public class Customer {
    /**
     * The Customer ID - primary key for the Customer table.
     */
    private int customerID;
    /**
     * The Customer name.
     */
    private String customerName;
    /**
     * The Customer address.
     */
    private String customerAddress;
    /**
     * The Customer postal code.
     */
    private String customerPostalCode;
    /**
     * The Customer phone number.
     */
    private String customerPhoneNumber;

    /**
     * The date that the Customer was created.
     */
    private Timestamp customerCreateDate;
    /**
     * The user that created the Customer.
     */
    private String customerCreatedBy;
    /**
     * The date that the Customer was last updated.
     */
    private Timestamp customerLastUpdate;
    /**
     * The user that last updated the Customer.
     */
    private String customerLastUpdatedBy;
    /**
     * The Customer division ID.
     */
    private int customerDivisionID;


    /**
     * This is the constructor for a new Customer. It takes in the customer ID, customer name, customer address, customer postal code, and other parameters.
     * Please note here that all of the time-related objects are also of type Timestamp, just as in the Appointment class.
     * Again, this leveled the playing field as far as conversions went, and made it easier to work with the data.
     *
     * @param customerID            the customer ID
     * @param customerName          the customer name
     * @param customerAddress       the customer address
     * @param customerPostalCode    the customer postal code
     * @param customerPhoneNumber   the customer phone number
     * @param customerCreateDate    the customer create date
     * @param customerCreatedBy     the customer created by
     * @param customerLastUpdate    the customer last update
     * @param customerLastUpdatedBy the customer last updated by
     * @param customerDivisionID    the customer division ID
     */
    public Customer(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber,
                    Timestamp customerCreateDate, String customerCreatedBy, Timestamp customerLastUpdate, String customerLastUpdatedBy, int customerDivisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerCreateDate = customerCreateDate;
        this.customerCreatedBy = customerCreatedBy;
        this.customerLastUpdate = customerLastUpdate;
        this.customerLastUpdatedBy = customerLastUpdatedBy;
        this.customerDivisionID = customerDivisionID;
    }


    /**
     * Gets customer ID.
     *
     * @return the customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets customer ID.
     *
     * @param customerID the customer ID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets customer name.
     *
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets customer address.
     *
     * @return the customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * Sets customer address.
     *
     * @param customerAddress the customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * Gets customer postal code.
     *
     * @return the customer postal code
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * Sets customer postal code.
     *
     * @param customerPostalCode the customer postal code
     */
    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    /**
     * Gets customer phone number.
     *
     * @return the customer phone number
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     * Sets customer phone number.
     *
     * @param customerPhoneNumber the customer phone number
     */
    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    /**
     * Gets customer create date.
     *
     * @return the customer create date
     */
    public Timestamp getCustomerCreateDate() {
        return customerCreateDate;
    }

    /**
     * Sets customer create date.
     *
     * @param customerCreateDate the customer create date
     */
    public void setCustomerCreateDate(Timestamp customerCreateDate) {
        this.customerCreateDate = customerCreateDate;
    }

    /**
     * Gets customer created by.
     *
     * @return the customer created by
     */
    public String getCustomerCreatedBy() {
        return customerCreatedBy;
    }

    /**
     * Sets customer created by.
     *
     * @param customerCreatedBy the customer created by
     */
    public void setCustomerCreatedBy(String customerCreatedBy) {
        this.customerCreatedBy = customerCreatedBy;
    }

    /**
     * Gets customer last update.
     *
     * @return the customer last update
     */
    public Timestamp getCustomerLastUpdate() {
        return customerLastUpdate;
    }

    /**
     * Sets customer last update.
     *
     * @param customerLastUpdate the customer last update
     */
    public void setCustomerLastUpdate(Timestamp customerLastUpdate) {
        this.customerLastUpdate = customerLastUpdate;
    }

    /**
     * Gets customer last updated by.
     *
     * @return the customer last updated by
     */
    public String getCustomerLastUpdatedBy() {
        return customerLastUpdatedBy;
    }

    /**
     * Sets customer last updated by.
     *
     * @param customerLastUpdatedBy the customer last updated by
     */
    public void setCustomerLastUpdatedBy(String customerLastUpdatedBy) {
        this.customerLastUpdatedBy = customerLastUpdatedBy;
    }

    /**
     * Gets customer division ID.
     *
     * @return the customer division ID
     */
    public int getCustomerDivisionID() {
        return customerDivisionID;
    }

    /**
     * Sets customer division ID.
     *
     * @param customerDivisionID the customer division ID
     */
    public void setCustomerDivisionID(int customerDivisionID) {
        this.customerDivisionID = customerDivisionID;
    }


}
