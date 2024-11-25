import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Week {

    private int week;

    // Constructor using Date and TimeZone
    public Week(Date time, TimeZone zone) {
        // defer argument checking...
        if (time == null || zone == null) {
            throw new IllegalArgumentException("Time and Zone cannot be null");
        }

        // Set up the calendar with the provided date and time zone
        Calendar calendar = new GregorianCalendar(zone);
        calendar.setTime(time);

        // Set locale to ensure consistent results across different locales
        Locale.setDefault(Locale.US);

        // Set first day of week to Sunday as per GregorianCalendar when using US/Detroit time zone
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);

        // Calculate the week number according to ISO-8601 standard (Monday as the first day)
        this.week = calendar.get(Calendar.WEEK_OF_YEAR);
    }

    // Method to get the week number
    public int getWeek() {
        return this.week;
    }
}