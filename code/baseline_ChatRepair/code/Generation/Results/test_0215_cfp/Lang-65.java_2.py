private static void modify(Calendar val, int field, boolean round) {
    // Check for valid year range
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    if (field == Calendar.MILLISECOND) return; // No need to truncate

    // Manually truncate milliseconds to zero
    if (val.get(Calendar.MILLISECOND) != 0) {
        val.set(Calendar.MILLISECOND, 0);
    }

    if (field == Calendar.SECOND) return; // Stop if only truncating to seconds

    // Manually truncate seconds to zero
    if (val.get(Calendar.SECOND) != 0) {
        val.set(Calendar.SECOND, 0);
    }

    if (field == Calendar.MINUTE) return; // Stop if only truncating to minutes

    // Manually truncate minutes to zero
    if (val.get(Calendar.MINUTE) != 0) {
        val.set(Calendar.MINUTE, 0);
    }
    
    // Further truncation fields continue here...
    // Resetting hours or day will depend on the value of `field` passed
}

