import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class Week {
    private Calendar calendar;

    public Week(Date time) {
        this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
    }

    public Week(Date time, TimeZone zone) {
        this(time, zone, Locale.getDefault());
    }

    public Week(Date time, TimeZone zone, Locale locale) {
        this.calendar = new GregorianCalendar(zone, locale);
        this.calendar.setTime(time);
        if (this.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY &&
            this.calendar.get(Calendar.WEEK_OF_YEAR) == 1 &&
            !isFirstDayOfWeek(thiscalendar.getTime())) {
            // If the first day of the year is a Sunday and we are not in week 53,
            // then move to the next week.
            this.calendar.add(Calendar.DATE, 7);
        }
    }

    private boolean isFirstDayOfWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
    }

    public int getWeek() {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }
}