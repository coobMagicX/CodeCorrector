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

    // Correct the calculation for hours in minutes
    int totalMinutes = hoursOffset * 60;
    if (totalMinutes == Integer.MIN_VALUE) { // Check for overflow
        throw new ArithmeticException("Hours offset is too large");
    }
    
    // Adjust minutesOffset based on totalMinutes sign
    int adjustedMinutes = (minutesOffset + totalMinutes) % 1440;
    if (adjustedMinutes < 0) {
        adjustedMinutes += 1440; // Convert negative to positive offset
    }

    // Calculate the offset in milliseconds safely
    long offsetMillis = FieldUtils.safeMultiply(adjustedMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
    
    // Check for overflow of offsetMillis
    if (offsetMillis > Integer.MAX_VALUE || offsetMillis < Integer.MIN_VALUE) {
        throw new ArithmeticException("Offset is too large");
    }

    return forOffsetMillis((int) offsetMillis);
}