import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.field.FieldUtils;

public class DateTimeZoneUtil {
    public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
        if (hoursOffset == 0 && minutesOffset == 0) {
            return DateTimeZone.UTC;
        }
        if (hoursOffset < -23 || hoursOffset > 23) {
            throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
        }
        if (minutesOffset < 0 || minutesOffset > 59) {
            throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
        }
        int totalMinutesOffset;
        int hoursInMinutes = hoursOffset * 60;
        if (hoursOffset < 0) {
            totalMinutesOffset = hoursInMinutes - minutesOffset;
        } else {
            totalMinutesOffset = hoursInMinutes + minutesOffset;
        }
        int offset;
        try {
            offset = FieldUtils.safeMultiply(totalMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        } catch (ArithmeticException ex) {
            throw new IllegalArgumentException("Offset is too large", ex);
        }
        return DateTimeZone.forOffsetMillis(offset);
    }
}