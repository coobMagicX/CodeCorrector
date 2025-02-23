import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public String format(Date date) {
    Calendar calendar = new GregorianCalendar(mTimeZone);
    calendar.setMinimalDaysInFirstWeek(4);          // ISO 8601 standard
    calendar.setFirstDayOfWeek(Calendar.MONDAY);    // ISO 8601 standard
    calendar.setTime(date);

    StringBuffer buffer = new StringBuffer(mMaxLengthEstimate);
    return applyRules(calendar, buffer).toString();
}

private StringBuffer applyRules(Calendar calendar, StringBuffer buffer) {
    // Assuming the