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
    
    if ((hoursOffset > 0 && minutesOffset < 0) || (hoursOffset < 0 && minutesOffset > 0)) {
        throw new IllegalArgumentException("Minutes should have the same sign as hours or be zero when specifying time zone offset.");
    }

    int offset = 0;
    try {
        // Compute total minutes offset correctly by adding the hours and minutes together
        int totalMinutesOffset = hoursOffset * 60 + minutesOffset;
        offset = totalMinutesOffset * DateTimeConstants.MILLIS_PER_MINUTE;
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    
    return forOffsetMillis(offset);
}
