private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Initialize roundUp variable from the start
    boolean roundUp = round;

    if (field == DateUtils.SEMI_MONTH) {
        // Special handling for semi-month requires accessing the date field
        int date = val.get(Calendar.DATE);
        if (round && (date <= 15 || date > 15)) {
            val.add(Calendar.DATE, (date > 15) ? 16 - date : 1 - date);
        } else {
            val.set(Calendar.DATE, (date > 15) ? 16 : 1);
        }
        return; // Semi-month adjustment handled specifically - return immediately
    }

    // Query to check if we are setting to ZERO for lower fields
    if (field <= Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field <= Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }
    if (field <= Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }
    if (field <= Calendar.HOUR_OF_DAY) {
        val.set(Calendar.HOUR_OF_DAY, val.get(Calendar.HOUR_OF_DAY) / 12 * 12); // effectively zero out hour within AM/PM
    }

    if (field <= Calendar.DAY_OF_MONTH) {
        val.set(Calendar.HOUR_OF_DAY, 0); // Ensure no interference due to DOY adjustment across DST
    }
}
