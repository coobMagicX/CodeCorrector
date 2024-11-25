public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) throws IllegalArgumentException {
    if (hoursOffset < -23 || hoursOffset > 23) {
        throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
    }
    if (minutesOffset < 0 || minutesOffset > 59) {
        throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
    }
    int offset = 0;
    try {
        long hoursInMilliseconds = FieldUtils.safeMultiply(hoursOffset, DateTimeConstants.MILLIS_PER_HOUR);
        int adjustedMinutesOffset = (minutesOffset + (hoursOffset < 0 ? -hoursInMilliseconds : hoursInMilliseconds));
        if (adjustedMinutesOffset >= 0) {
            offset = FieldUtils.safeMultiply(adjustedMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        } else {
            // Handle negative minutes offset when hours offset is positive
            long absAdjustedMinutesOffset = Math.abs(adjustedMinutesOffset);
            int remainingHours = (int) (absAdjustedMinutesOffset / 60);
            int remainingMinutes = (int) (absAdjustedMinutesOffset % 60);
            if (remainingMinutes < 0) {
                remainingHours--;
                remainingMinutes += 60;
            }
            long hoursInMillisecondsToAdd = FieldUtils.safeMultiply(remainingHours, DateTimeConstants.MILLIS_PER_HOUR);
            offset = hoursInMillisecondsToAdd + FieldUtils.safeMultiply(remainingMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
        }
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}