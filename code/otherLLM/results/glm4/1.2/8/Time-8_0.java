public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < -59 || minutesOffset > 59) { // Adjusted to allow negative minutes
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    int offset = 0;
    try {
        int hoursInMinutes = hoursOffset * 60;
        // Handle the case where minutesOffset is negative
        if (hoursInMinutes < 0 && minutesOffset < 0) {
            offset = FieldUtils.safeMultiply((Math.abs(hoursInMinutes) - Math.abs(minutesOffset)), DateTimeConstants.MILLIS_PER_MINUTE);
        } else if (hoursInMinutes > 0 || minutesOffset >= 0) { // Positive or zero total minutes
            offset = FieldUtils.safeMultiply(hoursInMinutes + minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        } else { // Negative total minutes
            offset = FieldUtils.safeMultiply(Math.abs(hoursInMinutes - minutesOffset), DateTimeConstants.MILLIS_PER_MINUTE);
        }
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}