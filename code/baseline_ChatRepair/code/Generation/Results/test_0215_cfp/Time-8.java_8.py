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
    try {
        totalMinutesOffset = hoursOffset * 60;
        if (hoursOffset < 0 && minutesOffset > 0) {
            // If hours are negative but minutes are positive, we should effectively "add" minutes
            totalMinutesOffset = totalMinutesOffset + minutesOffset;
        } else if (hoursOffset > 0 && minutesOffset < 0) {
            // If hours are positive but minutes are negative, we should effectively "subtract" minutes
            totalMinutesOffset = totalMinutesOffset + minutesOffset;
        } else {
            // Otherwise, just normally add the minutes (even if both are negative)
            totalMinutesOffset = totalMinutesOffset + minutesOffset;
        }

        int offset = FieldUtils.safeMultiply(totalMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
        return forOffsetMillis(offset);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
}
