public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    
    // Validate input hours and minutes separately.
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < 0 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    
    int offset = 0;
    try {
        int hoursInMinutes = hoursOffset * 60;
        // Correctly handle edge cases where hours or minutes are negative.
        if (hoursInMinutes < 0) {
            minutesOffset += -hoursInMinutes; // Add the absolute value to minutes
        } else {
            minutesOffset -= hoursInMinutes; // Subtract hours from minutes
        }
        
        offset = FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    
    return forOffsetMillis(offset);
}