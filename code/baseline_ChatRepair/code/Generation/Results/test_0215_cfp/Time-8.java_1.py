public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    // Check extreme hour values
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    
    // Check minute values when hours are not zero
    if (hoursOffset != 0 && (minutesOffset < 0 || minutesOffset > 59)) {
        throw new IllegalArgumentException("Minutes out of range when hour offset is non-zero: " + minutesOffset);
    }
    
    // Check minute values when hours are zero
    if (hoursOffset == 0 && (minutesOffset < -59 || minutesOffset > 59)) {
        throw new IllegalArgumentException("Minutes out of range when hour offset is zero: " + minutesOffset);
    }
    
    // Calculate the full offset in milliseconds
    int totalOffset = 0;
    try {
        int overallMinutesOffset = hoursOffset * 60 + minutesOffset;
        totalOffset = FieldUtils.safeMultiply(overallMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    
    // Return the corresponding DateTimeZone
    return forOffsetMillis(totalOffset);
}
