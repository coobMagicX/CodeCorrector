public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    
    // When hours are negative, minutes should be allowed to be negative but within the range of 0 to 59.
    if (hoursOffset < 0 && (minutesOffset < -59 || minutesOffset > 0)) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    
    // When hours are positive, minutes must be non-negative and within the range of 0 to 59.
    if (hoursOffset > 0 && (minutesOffset < 0 || minutesOffset > 59)) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    
    // When hours are zero, any minute value from -59 to 59 should be accepted.
    if (hoursOffset == 0 && (minutesOffset < -59 || minutesOffset > 59)) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    
    try {
        int totalMinutes = hoursOffset * 60 + minutesOffset;
        int offsetMillis = FieldUtils.safeMultiply(totalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
        return forOffsetMillis(offsetMillis);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Combined offset is too large", ex);
    }
}
