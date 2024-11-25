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

    int totalMinutes = FieldUtils.safeMultiply(hoursOffset, 60) + minutesOffset;
    if (totalMinutes < 0 || totalMinutes > 1440) { // Ensure the total offset is within a valid day
        throw new IllegalArgumentException("Total minutes out of range: " + totalMinutes);
    }

    int offsetMillis = FieldUtils.safeMultiply(totalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);

    try {
        return forOffsetMillis(offsetMillis); // Call the method to get DateTimeZone
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Total offset is too large");
    }
}