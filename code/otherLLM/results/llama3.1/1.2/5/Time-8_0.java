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
        int hoursInMinutes = hoursOffset * 60;
        int minutesOffsetSign = (hoursInMinutes < 0) ? -1 : 1;
        int adjustedMinutesOffset = hoursInMinutes * minuteOffsetSign + minutesOffset * minuteOffsetSign;
        if (adjustedMinutesOffset > DateTimeConstants.MILLIS_PER_HOUR) {
            throw new IllegalArgumentException("Offset is too large");
        }
        offset = FieldUtils.safeMultiply(adjustedMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}