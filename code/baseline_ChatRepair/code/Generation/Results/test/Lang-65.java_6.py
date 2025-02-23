private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // truncate milliseconds
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }

    // truncate seconds
    if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }

    // truncate minutes
    if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }

    // Check if the timezone offset has changed due to the truncation (presence of daylight saving time changes etc.)
    // After each truncation step, reevaluate the timezone offset
    TimeZone tz = val.getTimeZone();
    int newOffset = tz.getOffset(val.getTimeInMillis());
    val.set(Calendar.ZONE_OFFSET, newOffset);

    boolean roundUp = false;
    // rest of the code remains unchanged from previous solution
}
