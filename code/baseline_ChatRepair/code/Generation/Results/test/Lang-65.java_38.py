private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Manually truncate milliseconds to 0
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }

    // Manually truncate seconds to 0
    if (field <= Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }

    // Manually truncate minutes to 0
    if (field <= Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }

    // For dates and larger units, reset smaller fields to 0
    switch(field) {
        case Calendar.HOUR_OF_DAY:
            val.set(Calendar.MINUTE, 0);
            // Fall through
        case Calendar.MINUTE:
            val.set(Calendar.SECOND, 0);
            // Fall through
        case Calendar.SECOND:
            val.set(Calendar.MILLISECOND, 0);
            break;
    }

    boolean roundUp = false;
    // Your subsequent logic...
}
