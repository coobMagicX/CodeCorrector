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
    int offset = FieldUtils.safeMultiply(hoursOffset, DateTimeConstants.MILLIS_PER_HOUR)
                 + FieldUtils.safeMultiply(minutesOffset, DateTimeConstants.MILLIS_PER_MINUTE);
    
    if (offset < -2147483648 || offset > 2147483647) {
        throw new IllegalArgumentException("Offset is too large");
    }
    
    return forOffsetMillis(offset);
}