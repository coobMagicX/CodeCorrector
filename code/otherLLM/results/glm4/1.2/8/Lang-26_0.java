import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter {
    private static final String PATTERN = "EEEE, week 'W'w"; // EEEE for full weekday name and 'w' for the ISO-8601 week number
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
    private static final Locale LOCALE = new Locale("sv", "SE");
    private static FastDateFormat fastDateFormat;

    public static synchronized FastDateFormat getInstance() {
        if (fastDateFormat == null) {
            fastDateFormat = getInstance(PATTERN, TIME_ZONE, LOCALE);
        }
        return fastDateFormat;
    }

    public String format(Date date) {
        Calendar c = new GregorianCalendar(TIME_ZONE, LOCALE);
        c.setTime(date);

        // Adjust for the Swedish locale's first day of the week (Monday) and minimal days in first week
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(4);

        return getInstance().format(c);
    }

    public static FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
        FastDateFormat emptyFormat = new FastDateFormat(pattern, timeZone, locale);
        FastDateFormat format = cInstanceCache.get(emptyFormat);
        if (format == null) {
            format = emptyFormat;
            format.init();  // convert shell format into usable one
            cInstanceCache.put(format, format);  // this is OK!
        }
        return format;
    }

    private static final java.util.Map<FastDateFormat, FastDateFormat> cInstanceCache = new java.util.HashMap<>();
}