import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Week {

    private Date time;
    private TimeZone zone;
    private Locale locale;

    public Week(Date time, TimeZone zone) {
        // defer argument checking...
        this(time, zone, Locale.getDefault());
    }

    public Week(Date time, TimeZone zone, Locale locale) {
        if (time == null || zone == null || locale == null) {
            throw new IllegalArgumentException("Time, zone and locale cannot be null");
        }
        this.time = time;
        this.zone = zone;
        this.locale = locale;

        // Reset the calendar to the provided date and time zone
        Calendar calendar = Calendar.getInstance(zone, locale);
        calendar.setTime(time);

        // Set the first day of week according to the provided locale
        int startOfWeek = calendar.getFirstDayOfWeek();

        // Calculate the week number based on the first day of the week
        calendar.set(Calendar.DAY_OF_YEAR, 1); // Set to the first day of the year
        calendar.add(Calendar.DATE, calendar.get(Calendar.DAY_OF_WEEK) - startOfWeek);
        this.weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private int weekNumber;

    // Getters and setters for time, zone, locale, and weekNumber if needed
}