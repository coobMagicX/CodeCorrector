import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    private static final String DATE_FORMAT = "EEEE, week 'W'ww";
    private static final Locale SWEDISH_LOCALE = new Locale("sv", "SE");

    public String format(Date date) {
        Calendar c = Calendar.getInstance(SWEDISH_LOCALE);
        c.setTime(date);

        // Ensure the calendar's first day of week is set to Monday, which is required for the week number calculation
        c.setFirstDayOfWeek(Calendar.MONDAY);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, SWEDISH_LOCALE);
        return dateFormat.format(c.getTime());
    }

    public static void main(String[] args) {
        Date date = new Date(1262304000000L); // January 1, 2010 at 12:00 PM CET
        DateFormatter formatter = new DateFormatter();
        String formattedDate = formatter.format(date);
        System.out.println(formattedDate);
    }
}