private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    
    // Preserving the original timezone.
    TimeZone originalTimeZone = val.getTimeZone();
    // Set the timezone to UTC for processing to avoid DST complications.
    val.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Truncate milliseconds
    if (field <= Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    // Truncate seconds
    if (field <= Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }
    // Truncate minutes
    if (field <= Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }

    // Reset the original TimeZone
    val.setTimeZone(originalTimeZone);

    // Continue other processing
    // ...

}
