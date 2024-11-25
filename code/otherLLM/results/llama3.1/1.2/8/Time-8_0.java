public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    if (hoursOffset < -24 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < 0 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    int offset = 0;
    try {
        int hoursInMinutes = FieldUtils.safeMultiply(hoursOffset, 60); // Corrected calculation
        if (hoursInMinutes < 0) {
            minutesOffset = -FieldUtils.safeAdd(FieldUtils.safeMultiply(hoursInMinutes, -1), minutesOffset);
        } else {
            minutesOffset = FieldUtils.safeAdd(FieldUtils.safeMultiply(hoursInMinutes, 1), minutesOffset);
        }
        offset = FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}