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
        // Correcting the logic to handle negative minutes properly
        int totalMinutesOffset = hoursOffset * 60 + minutesOffset;
        if (totalMinutesOffset < 0) {
            // Adjust for wrap-around when subtracting a large number of minutes
            int absoluteTotalMinutes = Math.abs(totalMinutesOffset);
            int extraHours = absoluteTotalMinutes / 1440; // Get the extra hours from the total minutes
            int modTotalMinutes = absoluteTotalMinutes % 1440;
            offset = FieldUtils.safeMultiply(modTotalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
            if (extraHours > 0) {
                for (int i = 0; i < extraHours; i++) {
                    offset -= FieldUtils.safeMultiply(60 * 60 * 1000, DateTimeConstants.MILLIS_PER_SECOND); // Subtract the hours
                }
            }
        } else {
            // Positive total minutes calculation
            offset = FieldUtils.safeMultiply(totalMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        }
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}