package komunitas.java.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_TIME_FORMATTER) : "";
    }

    public static LocalDate parseDate(String dateString) {
        return dateString != null && !dateString.isEmpty() 
                ? LocalDate.parse(dateString, DATE_FORMATTER) 
                : null;
    }

    public static LocalDateTime parseDateTime(String dateTimeString) {
        return dateTimeString != null && !dateTimeString.isEmpty() 
                ? LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER) 
                : null;
    }
}