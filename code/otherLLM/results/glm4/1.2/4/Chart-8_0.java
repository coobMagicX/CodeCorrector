import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Week {

    private Date time;
    private TimeZone zone;
    private Locale locale;

    public Week(Date time, TimeZone zone, Locale locale) {
        if (time == null || zone == null || locale == null) {
            throw new IllegalArgumentException("Time, Zone, and Locale must not be null");
        }
        this.time = new Date(time.getTime()); // Clone the date to avoid external changes
        this.zone = zone;
        this.locale = locale;
    }

    public int getWeekNumber() {
        java.util.Calendar calendar = Calendar.getInstance(this.zone, this.locale);
        calendar.setTime(this.time);

        // Set firstDayOfWeek to be Sunday as per ISO-8601 standard
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    // Getters and setters for time, zone, and locale (if needed)

    public static void main(String[] args) {
        Date date = new Date(); // Example: current date
        TimeZone timeZone = TimeZone.getDefault();
        Locale locale = Locale.getDefault();

        Week week = new Week(date, timeZone, locale);
        System.out.println("Week number for " + date.toString() + " is " + week.getWeekNumber());
    }
}