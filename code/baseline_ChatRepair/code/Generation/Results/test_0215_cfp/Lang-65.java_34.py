private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    //
    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods.

    if (field <= Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field <= Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }
    if (field <= Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }

    // Set the calendar's AM/PM field appropriately
    if (field == Calendar.AM_PM) {
        if (val.get(Calendar.HOUR_OF_DAY) >= 12) {
            val.set(Calendar.HOUR_OF_DAY, 12); // PM, by setting to 12 you reset everything else in HOUR_OF_DAY to zero
        } else {
            val.set(Calendar.HOUR_OF_DAY, 0); // AM, by setting to 0 you reset everything else in HOUR_OF_DAY to zero
        }
    }
    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    // Further original code logic...
}
