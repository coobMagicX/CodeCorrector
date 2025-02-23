import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter {
    private TimeZone mTimeZone;
    private int mMaxLengthEstimate;

    public DateFormatter(TimeZone timeZone, int maxLengthEstimate) {
        this.mTimeZone = timeZone;
        this.mMaxLengthEstimate = maxLengthEstimate;
    }

    public String format