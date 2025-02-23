private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Switch to UTC to avoid DST changes
    TimeZone originalTimeZone = val.getTimeZone();
    val.setTimeZone(TimeZone.getTimeZone("UTC"));

    // Store the fields to be reset to avoid DST issues.
    int previousYear = val.get(Calendar.YEAR);
    int previousMonth = val.get(Calendar.MONTH);
    int previousDate = val.get(Calendar.DATE);
    int previousHourOfDay = val.get(Calendar.HOUR_OF_DAY);
    int previousMinute = val.get(Calendar.MINUTE);
    int previousSecond = val.get(Calendar.SECOND);

    // Truncate according to the specified field
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } else {
        // Handle other cases or throw an exception if the field is not supported
        throw new IllegalArgumentException("The field " + field + " is not supported for truncation");
    }

    // Reset the calendar's timezone to its original with DST corrections, if necessary.
    val.set(previousYear, previousMonth, previousDate, previousHourOfDay, previousMinute, previousSecond);
    val.setTimeZone(originalTimeZone);
}
