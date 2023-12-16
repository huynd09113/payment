import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CommonFunction {

    public static boolean isValidDateFormat(String dateString) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

        try {
            LocalDate.parse(dateString, dateFormatter);
            return true; // If the parsing succeeds, it's a valid date
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
