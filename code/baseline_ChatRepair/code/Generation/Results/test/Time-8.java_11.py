public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < -59 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    
    // Constraint: Minutes should have the same sign as hours if hours is not zero
    if ((hoursOffset > 0 && minutesOffset < 0) || (hoursOffset < 0 && minutesOffset > 0)) {
        throw new IllegalArgumentException("Minutes should have the same sign as hours when both are non-zero.");
    }

    int totalMinutesOffset = hoursOffset * 60 + minutesOffset;
    int offsetMillis;
    try {
        offsetMillis = FieldUtils.safeMultiply(totalMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }

    return forOffsetMillis(offsetMillis);
}
