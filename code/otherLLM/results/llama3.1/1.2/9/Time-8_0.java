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
        // Correctly calculate minutesOffset when converting hoursOffset to minutes
        int hoursInMinutes = hoursOffset * 60;
        if (hoursInMinutes < -360 || hoursInMinutes > 720) { // Check for overflow in both positive and negative directions
            throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
        }
        minutesOffset = hoursInMinutes + minutesOffset; // Correctly calculate minutesOffset
        
        offset = FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE); // Validate that FieldUtils.safeMultiply does not cause overflow or underflow issues
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}