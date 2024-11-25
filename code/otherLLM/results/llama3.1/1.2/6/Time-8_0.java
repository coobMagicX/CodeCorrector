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
        int hoursInMinutes = hoursOffset * 60; // Convert hours to minutes
        if (hoursInMinutes < 0) {
            // Correctly handle negative offsets by adding the absolute value of hoursInMinutes and the opposite of minutesOffset
            offset = FieldUtils.safeMultiply(-hoursInMinutes, DateTimeConstants.MILLIS_PER_MINUTE); // Use -hoursInMinutes for correct calculation when hoursOffset is negative
        } else {
            int totalMinutes = hoursInMinutes + minutesOffset;
            if (totalMinutes > 0) { // Check if the total minutes are positive before performing the multiplication
                offset = FieldUtils.safeMultiply(totalMinutes, DateTimeConstants.MILLIS_PER_MINUTE);
            } else { // Handle edge case where the total minutes are not positive
                throw new IllegalArgumentException("Offset is too large");
            }
        }
    } catch (ArithmeticException ex) {
        throw new IllegalArgumentException("Offset is too large");
    }
    return forOffsetMillis(offset);
}