package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class LogHelper handles all the logging for the program.
 * Specifically, it creates a log file called "login_activity.txt" in the root directory of the program.
 */
public class LogHelper {

    /**
     * The log file initialization and name. The program will create this file if it doesn't exist already.
     */
    private static final String LOG_FILE = "login_activity.txt";


    /**
     * This method logs login activity to the log file.
     * The path is relative to the root directory of the program.
     * I wanted to make sure that the messages were timestamped in the user's local time zone.
     * I also formatted the time for readability, as well as included the UTC offset.
     * It writes a header to the file if the file is empty.
     *
     * @param message log-in message - timestamp, username, and success/failure
     */
    public void log(String message) {
        try {
            String rootDir = Paths.get(".").toAbsolutePath().normalize().toString();
            String logPath = Paths.get(rootDir, LOG_FILE).toString();
            FileWriter writer = new FileWriter(logPath, true);
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
            String timestamp = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss'     UTC OFFSET: 'Z '('zzz')'"));

            // Write the header text to the file if it's empty
            File logFile = new File(logPath);
            if (logFile.length() == 0) {
                writer.write("=== Log File Header ===\n\n");
            }

            // Write the log message to the file
            writer.write(timestamp + "\n" + message + "\n\n\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
