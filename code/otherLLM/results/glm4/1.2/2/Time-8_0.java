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

    // Calculate total minutes with handling for negative values
    int totalMinutes = FieldUtils.safeAdd(hoursOffset * 60, minutesOffset);

    // Adjust total minutes to be within the range [0, 1440)
    if (totalMinutes < 0) {
        totalMinutes += 1440;
    }

    // Convert total minutes to milliseconds for offset
    int offset = FieldUtils.safeMultiply(totalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);

    return forOffsetMillis(offset);
}