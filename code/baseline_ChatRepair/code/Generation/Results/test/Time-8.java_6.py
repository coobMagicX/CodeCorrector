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
    if (hoursOffset > 0 && minutesOffset < 0 || hoursOffset < 0 && minutesOffset > 0) {
        throw new IllegalArgumentException("Signs of hour and minute offsets must not differ");
    }
    
    int offset = 0;
    try {
        int totalMinutesOffset = hoursOffset * 60 + minutesOffset; // Ensure the correct addition/subtraction of minutes
        offset = FieldUtils.safeMultiply(totalMinutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}
