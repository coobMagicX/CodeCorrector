private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Truncate milliseconds
    if (field == Calendar.SECOND || field == Calendar.MINUTE || field == Calendar.HOUR_OF_DAY) {
        val.set(Calendar.MILLISECOND, 0);
    }

    // Truncate seconds
    if (field == Calendar.MINUTE || field == Calendar.HOUR_OF_DAY) {
        val.set(Calendar.SECOND, 0);
    }

    // Truncate minutes
    if (field == Calendar.HOUR_OF_DAY) {
        val.set(Calendar.MINUTE, 0);
    }

    // Rest of your code...
}
