public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    // Check if both hoursOffset and minutesOffset are zero.
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    
    // Validate the range of hoursOffset.
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    
    // Validate the range of minutesOffset.
    if (minutesOffset < 0 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }

    int totalOffsetMinutes = FieldUtils.safeMultiply(hoursOffset, 60) + minutesOffset; // Calculate total offset in minutes
    long offsetMillis;

    try {
        // Convert the total offset in minutes to milliseconds.
        offsetMillis = FieldUtils.safeMultiply(totalOffsetMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
        
        if (offsetMillis < 0) {
            throw new IllegalArgumentException("Offset is too large");
        }
    } catch (ArithmeticException ex) {
        // This exception should not be caught here because it's already handled by FieldUtils.safeMultiply.
        throw new RuntimeException(ex); // Use RuntimeException to prevent the caller from catching this specific type of error.
    }

    return forOffsetMillis(offsetMillis);
}