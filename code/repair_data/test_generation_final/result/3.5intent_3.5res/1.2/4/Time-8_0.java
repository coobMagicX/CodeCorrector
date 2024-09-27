public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    // Check for zero offset which always results in UTC
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    // Validate hour and minute ranges
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < -59 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    // Calculate total offset in minutes
    int totalMinutes = hoursOffset * 60 + minutesOffset;
    // Convert minutes to milliseconds
    int offset;
    try {
        offset = FieldUtils.safeMultiply(totalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    // Create and return the DateTimeZone with the calculated offset
    return forOffsetMillis(offset);
}
