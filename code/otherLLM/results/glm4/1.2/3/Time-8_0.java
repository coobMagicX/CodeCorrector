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
    
    // Ensure that the total offset is positive before converting to milliseconds.
    int totalMinutes = FieldUtils.safeAdd(FieldUtils.safeMultiply(hoursOffset, 60), minutesOffset);
    if (totalMinutes < 0) {
        hoursOffset = FieldUtils.safeSubtract(0, hoursOffset); // Reset hours to zero
        minutesOffset = FieldUtils.safeAdd(1440, totalMinutes); // Add 24 hours (1440 minutes)
    }

    int offset = FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    
    return forOffsetMillis(offset);
}