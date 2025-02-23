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

    // Calculate the total offset in minutes taking into account the sign of the hours.
    int totalMinutesOffset;
    if (hoursOffset < 0) {
        totalMinutesOffset = hoursOffset * 60 - Math.abs(minutesOffset);
    } else {
        totalMinutesOffset = hoursOffset * 60 + minutesOffset;
    }

    int offset;
    try {
        offset = FieldUtils.safeMultiply(totalMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }

    return forOffsetMillis(offset);
}
