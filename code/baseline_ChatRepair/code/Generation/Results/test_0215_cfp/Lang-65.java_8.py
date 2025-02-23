private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Clear smaller fields
    if (field == Calendar.YEAR) {
        val.clear(Calendar.MONTH);
    }
    if (field == Calendar.MONTH || field == Calendar.YEAR) {
        val.set(Calendar.DATE, 1);
    }
    if (field == Calendar.DATE || field == Calendar.MONTH || field == Calendar.YEAR) {
        val.set(Calendar.HOUR_OF_DAY, 0);
    }
    if (field == Calendar.HOUR_OF_DAY || field == Calendar.DATE || field == Calendar.MONTH || field == Calendar.YEAR) {
        val.clear(Calendar.MINUTE);
    }
    if (field == Calendar.MINUTE || field == Calendar.HOUR_OF_DAY || field == Calendar.DATE || field == Calendar.MONTH || field == Calendar.YEAR) {
        val.clear(Calendar.SECOND);
    }
    if (field == Calendar.SECOND || field == Calendar.MINUTE || field == Calendar.HOUR_OF_DAY || field == Calendar.DATE || field == Calendar.MONTH || field == Calendar.YEAR) {
        val.clear(Calendar.MILLISECOND);
    }

    boolean roundUp = false;
    // Additional logic for determining and handling rounding up...

    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                // Logic for handling rounding and special situations like SEMI_MONTH...
            }
        }
    }

    // More code to adjust the field values based on the rounding logic...
}
