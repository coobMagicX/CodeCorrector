public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset == 0 && minutesOffset == 0) {
        return DateTimeZone.UTC;
    }
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < -59 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    int totalMinutesOffset;
    if (hoursOffset == 0) {
        totalMinutesOffset = minutesOffset;
    } else if (minutesOffset < 0 && hoursOffset > 0) {
        totalMinutesOffset = hoursOffset * 60 + minutesOffset;
    } else if (minutesOffset > 0 && hoursOffset < 0) {
        totalMinutesOffset = hoursOffset * 60 + minutesOffset;
    } else {
        totalMinutesOffset = hoursOffset * 60 + minutesOffset;
    }
    int offset = 0;
    try {
        offset = FieldUtils.safeMultiply(totalMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}