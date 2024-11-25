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
        int hoursInMinutes = Math.abs(hoursOffset * 60); // Re-evaluate the absolute value
        if (hoursInMinutes < 0) {
            minutesOffset = -Math.abs(minutesOffset); // Corrected off-by-one error
            offset = FieldUtils.safeMultiply(-minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        } else {
            minutesOffset = hoursInMinutes + minutesOffset;
            offset = FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        }
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}