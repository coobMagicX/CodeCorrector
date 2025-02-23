private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Truncating milliseconds, seconds, and minutes depending on the chosen field
    if (field == Calendar.MILLISECOND) {
        return; // Nothing to truncate as we're already at the smallest considered unit
    }

    // Reset milliseconds always
    val.set(Calendar.MILLISECOND, 0);

    if (field == Calendar.SECOND) {
        return; // Stop truncating more as we're at the desired unit
    }

    // Reset seconds
    val.set(Calendar.SECOND, 0);

    if (field == Calendar.MINUTE) {
        return; // Stop truncating more as we're at the desired unit
    }

    // Reset minutes
    val.set(Calendar.MINUTE, 0);

    if (field == Calendar.HOUR_OF_DAY) {
        return; // Stop truncating more as we're at the desired unit
    }

    // Reset hour of day
    val.set(Calendar.HOUR_OF_DAY, 0);

    if (field == Calendar.DATE) {
        return; // Stop truncating here as date (day of month) is the unit
    }

    // Reset day of month if necessary
    if (field == Calendar.MONTH) {
        val.set(Calendar.DATE, 1);
        return;  // Stop truncating here as month is the unit
    }

    if (field == DateUtils.SEMI_MONTH) {
        int day = val.get(Calendar.DATE);
        if (day >= 16) {
            val.set(Calendar.DATE, 16);
        } else {
            val.set(Calendar.DATE, 1);
        }
        return;
    }

    // For truncating to a full month or larger
    val.set(Calendar.DATE, 1);

    if (field == Calendar.YEAR) {
        val.set(Calendar.MONTH, 0);
        return; // no truncation past year necessary
    }

    // Handling cases when even larger fields like ERA might need truncation
    if (field == Calendar.ERA) {
        val.set(Calendar.YEAR, 1);
    }
}
