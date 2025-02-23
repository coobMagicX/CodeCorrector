import java.util.Calendar;
import java.util.TimeZone;

private static void modify(Calendar val, int field, boolean round, TimeZone tz) {
    val.setTimeZone(tz); // Ensure correct time zone set for operations

    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Truncation logic based on field specifics needed
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field <= Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }
    if (field <= Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }
    if (field <= Calendar.HOUR_OF_DAY) {
        val.set(Calendar.HOUR_OF_DAY, val.get(Calendar.HOUR_OF_DAY)); // No truncation, ensure correct hour of day
    }

    // Round logic based on further requirements might also need handling here
    // (Not implemented as specifics are not provided.)

    // Handle other fields if applicable
}
