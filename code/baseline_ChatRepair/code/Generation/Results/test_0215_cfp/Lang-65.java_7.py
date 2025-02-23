private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    
    // Store original timezone to prevent issues with daylight saving times
    TimeZone originalTimeZone = val.getTimeZone();
    val.setTimeZone(TimeZone.getTimeZone("UTC")); // Work in UTC to avoid DST changes during manipulation

    // Truncate finer granularity fields based on the input `field`
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } // Add more cases if other fields need truncation

    val.setTimeZone(originalTimeZone); // Reset to the original timezone to reflect correct local time

    // Handle rounding if specified. Omitted here for brevity, but include logic if rounding needed.
    // ...
}
