package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utilities.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class DB_Customers handles the database queries to load the customer tableview.
 * DB stands for... database..
 * This class didn't necessarily need to be in a separate dao package, but initial attempts at the project relied heavily on a dao package, so I kept it.
 */
public class DB_Customers {

    /**
     * This method loads the customer tableview by querying the database, loading the results into a Customer object, and adding that object to an observable list.
     *
     * @return the observable list of customers
     */
    public static ObservableList<Customer> loadCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        Connection conn = DatabaseHelper.connect();
        String query = "SELECT * FROM customers";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Division_ID")
                );

                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }


}
