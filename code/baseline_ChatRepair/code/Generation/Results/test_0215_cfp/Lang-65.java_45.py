import java.util.Calendar;

public static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Truncation logic
    switch (field) {
        case Calendar.MILLISECOND:
            break; // There's nothing to truncate under millisecond
        case Calendar.SECOND:
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.MINUTE:
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.HOUR_OF_DAY:
        case Calendar.HOUR:
            val.set(Calendar.MINUTE, 0);
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        default:
            throw new IllegalArgumentException("The field " + field + " is not supported");
    }

    // Add additional rounding logic below if necessary
}

