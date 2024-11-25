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
    
    int offset = hoursOffset * DateTimeConstants.MINUTES_PER_HOUR;
    offset += minutesOffset; // Add the minutes to the total minutes of the offset
    
    // Adjust for negative offsets
    if (hoursOffset < 0) {
        // If the hour offset is negative, we need to subtract the minute offset from it
        // This will effectively move the time zone further away from UTC in a Western direction.
        int absoluteMinutes = Math.abs(offset);
        if (absoluteMinutes > DateTimeConstants.MAX_OFFSET_MINUTES) {
            throw new IllegalArgumentException("Offset is too large");
        }
        offset -= minutesOffset;
    } else {
        // If the hour offset is positive, we need to add the minute offset to it
        // This will effectively move the time zone further away from UTC in an Eastern direction.
        if (offset > DateTimeConstants.MAX_OFFSET_MINUTES) {
            throw new IllegalArgumentException("Offset is too large");
        }
    }

    return forOffsetMillis(offset * DateTimeConstants.MILLIS_PER_MINUTE);
}