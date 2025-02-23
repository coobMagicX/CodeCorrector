public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }

    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }

    // Whether minutes are positive or negative, their absolute value should not exceed 59.
    if (Math.abs(minutesOffset) > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    
    // Compute the total offset in terms of milliseconds
    int totalOffsetMinutes = hoursOffset * 60 + minutesOffset;
    int offsetInMillis;
    try {
        offsetInMillis = FieldUtils.safeMultiply(totalOffsetMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    
    return forOffsetMillis(offsetInMillis);
}
