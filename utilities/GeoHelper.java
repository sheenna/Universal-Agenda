package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class GeoHelper primarily contains static maps that are used to populate the country and division combo boxes.
 * This was done to avoid having to make a database call every time the combo boxes are populated.
 * According to the scenario, the country and division combo boxes updated annually, and as the data doesn't change often, it was decided to hardcode the data.
 */
public class GeoHelper {
    /**
     * This Map contains the country names and their corresponding country IDs.
     * It was used in a previous iteration of the project. It is not currently used.
     * However, it is left in the code in case it is needed in the future.
     *
     */
    public static final Map<String, Integer> COUNTRY_TO_DIV_ID = new HashMap<>() {{
        put("US", 1);
        put("UK", 2);
        put("Canada", 3);
    }};
    /**
     * This Map contains the country IDs and their corresponding country names.
     * I  had just completed Data Structures and Algorithms when I began this project, and really couldn't get the idea of using a HashMap out of my head.
     */
    public static final Map<Integer, String> DIVLIST = new HashMap<>() {{
        put(1, "Alabama");
        put(2, "Arizona");
        put(3, "Arkansas");
        put(4, "California");
        put(5, "Colorado");
        put(6, "Connecticut");
        put(7, "Delaware");
        put(8, "District of Columbia");
        put(9, "Florida");
        put(10, "Georgia");
        put(11, "Idaho");
        put(12, "Illinois");
        put(13, "Indiana");
        put(14, "Iowa");
        put(15, "Kansas");
        put(16, "Kentucky");
        put(17, "Louisiana");
        put(18, "Maine");
        put(19, "Maryland");
        put(20, "Massachusetts");
        put(21, "Michigan");
        put(22, "Minnesota");
        put(23, "Mississippi");
        put(24, "Missouri");
        put(25, "Montana");
        put(26, "Nebraska");
        put(27, "Nevada");
        put(28, "New Hampshire");
        put(29, "New Jersey");
        put(30, "New Mexico");
        put(31, "New York");
        put(32, "North Carolina");
        put(33, "North Dakota");
        put(34, "Ohio");
        put(35, "Oklahoma");
        put(36, "Oregon");
        put(37, "Pennsylvania");
        put(38, "Rhode Island");
        put(39, "South Carolina");
        put(40, "South Dakota");
        put(41, "Tennessee");
        put(42, "Texas");
        put(43, "Utah");
        put(44, "Vermont");
        put(45, "Virginia");
        put(46, "Washington");
        put(47, "West Virginia");
        put(48, "Wisconsin");
        put(49, "Wyoming");
        put(52, "Hawaii");
        put(54, "Alaska");
        put(60, "Northwest Territories");
        put(61, "Alberta");
        put(62, "British Columbia");
        put(63, "Manitoba");
        put(64, "New Brunswick");
        put(65, "Nova Scotia");
        put(66, "Prince Edward Island");
        put(67, "Ontario");
        put(68, "Québec");
        put(69, "Saskatchewan");
        put(70, "Nunavut");
        put(71, "Yukon");
        put(72, "Newfoundland and Labrador");
        put(101, "England");
        put(102, "Wales");
        put(103, "Scotland");
        put(104, "Northern Ireland");
    }};

    /**
     * This List contains times as strings, and is used to populate the time related combo boxes.
     * These specific times were chosen because while it is not strict business times, it is a good representation of hours that one may be available, considering the global nature of the program.
     */
    public static final List<String> TIME_LIST = Arrays.asList(
            "04:00 AM", "04:30 AM", "05:00 AM", "05:30 AM",
            "06:00 AM", "06:30 AM", "07:00 AM", "07:30 AM", "08:00 AM", "08:30 AM",
            "09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM",
            "12:00 PM", "12:30 PM", "01:00 PM", "01:30 PM", "02:00 PM", "02:30 PM",
            "03:00 PM", "03:30 PM", "04:00 PM", "04:30 PM", "05:00 PM", "05:30 PM",
            "06:00 PM", "06:30 PM", "07:00 PM", "07:30 PM", "08:00 PM", "08:30 PM",
            "09:00 PM", "09:30 PM", "10:00 PM", "10:30 PM", "11:00 PM"
    );
    /**
     * A simple list of countries. This is used to populate the country combo box.
     */
    public static ObservableList<String> COUNTRIES = FXCollections.observableList(Arrays.asList("US", "UK", "Canada"));
    /**
     * This is a list of first level divisions specific to the US. This is used to populate the division combo box when the user selects the US as their country.
     */
    public static ObservableList<String> US_DIVISION_1 = FXCollections.observableList(Arrays.asList(
            "Alabama",
            "Arizona",
            "Arkansas",
            "California",
            "Colorado",
            "Connecticut",
            "Delaware",
            "District of Columbia",
            "Florida",
            "Georgia",
            "Idaho",
            "Illinois",
            "Indiana",
            "Iowa",
            "Kansas",
            "Kentucky",
            "Louisiana",
            "Maine",
            "Maryland",
            "Massachusetts",
            "Michigan",
            "Minnesota",
            "Mississippi",
            "Missouri",
            "Montana",
            "Nebraska",
            "Nevada",
            "New Hampshire",
            "New Jersey",
            "New Mexico",
            "New York",
            "North Carolina",
            "North Dakota",
            "Ohio",
            "Oklahoma",
            "Oregon",
            "Pennsylvania",
            "Rhode Island",
            "South Carolina",
            "South Dakota",
            "Tennessee",
            "Texas",
            "Utah",
            "Vermont",
            "Virginia",
            "Washington",
            "West Virginia",
            "Wisconsin",
            "Wyoming",
            "Hawaii",
            "Alaska"
    ));
    /**
     * This is a list of first level divisions specific to the UK. This is used to populate the division combo box when the user selects the UK as their country.
     */
    public static ObservableList<String> UK_DIVISION_2 = FXCollections.observableList(Arrays.asList("England", "Wales", "Scotland", "Northern Ireland"));
    /**
     * This is a list of first level divisions specific to Canada. This is used to populate the division combo box when the user selects Canada as their country.
     */
    public static ObservableList<String> CANADA_DIVISION_3 = FXCollections.observableList(Arrays.asList(
            "Northwest Territories",
            "Alberta",
            "British Columbia",
            "Manitoba",
            "New Brunswick",
            "Nova Scotia",
            "Prince Edward Island",
            "Ontario",
            "Québec",
            "Saskatchewan",
            "Nunavut",
            "Yukon",
            "Newfoundland and Labrador"
    ));
    /**
     * This Map maps the first level divisions to their respective countries. This is used to populate the division combo box when the user selects a country in accordance with the COUNTRY_TO_DIVISIONS map.
     */
    public static final Map<String, List<String>> COUNTRY_TO_DIVISIONS = new HashMap<>() {{
        put("US", US_DIVISION_1);
        put("UK", UK_DIVISION_2);
        put("Canada", CANADA_DIVISION_3);
    }};


}
