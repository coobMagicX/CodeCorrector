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
    
    // Correctly handle the case where both hours and minutes are zero
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    
    int offset = 0;
    try {
        int totalMinutes = FieldUtils.safeMultiply(hoursOffset, 60) + minutesOffset;
        
        // Correctly calculate the sign of the total minutes
        if (totalMinutes < 0) {
            hoursOffset = -hoursOffset;
            minutesOffset = -minutesOffset;
            totalMinutes = -totalMinutes;
        }
        
        offset = FieldUtils.safeMultiply(totalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    
    return forOffsetMillis(offset);
}