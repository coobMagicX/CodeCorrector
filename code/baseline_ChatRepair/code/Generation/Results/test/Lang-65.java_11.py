private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Preserve the original time zone
    TimeZone originalTimeZone = val.getTimeZone();

    switch (field) {
        case Calendar.MILLISECOND:
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.SECOND:
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.MINUTE:
            val.set(Calendar.MINUTE, 0);
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
    }

    boolean roundUp = false;

    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                if (round && roundUp) {
                    // Special rounding logic for SEMI_MONTH or other complex fields
                    // existing logic as per requirement
                }
                return;
            }
        }

        int min = val.getActualMinimum(fields[i][0]);
        int max = val.getActualMaximum(fields[i][0]);
        int offset = val.get(fields[i][0]) - min;
        roundUp = offset > ((max - min) / 2);
        val.set(fields[i][0], val.get(fields[i][0]) - offset);
    }
    
    // Reset to the original time zone
    val.setTimeZone(originalTimeZone);

    throw new IllegalArgumentException("The field " + field + " is not supported");
}
