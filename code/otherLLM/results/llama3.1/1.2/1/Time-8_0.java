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
    int offset = 0;
    try {
        int hoursInMinutes = hoursOffset * 60;
        // Validate the sign of minutesOffset in conjunction with hoursOffset
        if (hoursInMinutes < 0 && minutesOffset > 0) {
            throw new IllegalArgumentException("Invalid offset: " + printOffset(hoursInMinutes));
        } else if (hoursInMinutes >= 0 && minutesOffset < 0) {
            throw new IllegalArgumentException("Invalid offset: " + printOffset(hoursInMinutes));
        }
        // Ensure accurate calculation and handling of negative values for both hoursOffset and minutesOffset
        int sign = hoursInMinutes >= 0 ? 1 : -1;
        if (sign * minutesOffset < 0) {
            throw new IllegalArgumentException("Invalid offset: " + printOffset(hoursInMinutes));
        }
        // Corrected calculation
        offset = FieldUtils.safeMultiply((hoursInMinutes + minutesOffset), DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}