private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Truncate time-related fields by setting less significant fields to their minimum value.
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }

    // Additional handling to preserve hour of day during timezone transitions
    if (field == Calendar.HOUR_OF_DAY) {
        val.get(Calendar.HOUR_OF_DAY);  // Call to get() ensures computation
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }

    // Traverse through all possible fields to apply zero-truncation for larger fields
    boolean truncated = false;
    for (int[] fieldArray : fields) {
        for (int aField : fieldArray) {
            if (aField == field && !truncated) {
                truncated = true; // To ensure we don't truncate smaller significance fields
            } else if (truncated) {
                val.set(aField, val.getMinimum(aField));
            }
        }
    }

    // If the call is to round up and we are truncating a round-sensitive field
    if (round && field == DateUtils.SEMI_MONTH) {
        int date = val.get(Calendar.DATE);
        if (date <= 15) {
            val.set(Calendar.DATE, 1);
        } else {
            val.set(Calendar.DATE, 16);
        }
    }
}
