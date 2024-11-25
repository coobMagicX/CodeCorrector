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

    int totalMinutes = hoursOffset * 60 + minutesOffset;
    long totalMillis = FieldUtils.safeMultiply(totalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);

    if (totalMillis < Integer.MIN_VALUE || totalMillis > Integer.MAX_VALUE) {
        throw new IllegalArgumentException("Offset is too large");
    }

    int offset = (int) totalMillis;

    return forOffsetMillis(offset);
}