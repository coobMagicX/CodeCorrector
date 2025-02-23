private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    
    int[] fields = {
        Calendar.MILLISECOND,
        Calendar.SECOND,
        Calendar.MINUTE,
        Calendar.HOUR_OF_DAY,
        Calendar.DATE,
        Calendar.MONTH,
        Calendar.YEAR
    };

    // First set milliseconds to zero, all truncations will need this
    val.set(Calendar.MILLISECOND, 0);

    // Determine truncation level based on the input field
    int truncateLevel = -1;
    for (int i = 0; i < fields.length; i++) {
        if (fields[i] == field) {
            truncateLevel = i;
            break;
        }
    }

    if (truncateLevel == -1) {
        throw new IllegalArgumentException("Field " + field + " is not supported");
    }

    // Clear all smaller fields than the specified field
    for (int i = 0; i < truncateLevel; i++) {
        val.set(fields[i], val.getActualMinimum(fields[i]));
    }

    if (round) {
        if (field == Calendar.MONTH || field == Calendar.DATE || field == Calendar.YEAR ||
            field == Calendar.HOUR_OF_DAY || field == Calendar.MINUTE) {
            int halfwayPoint = (val.getActualMaximum(field) + 1 - val.getActualMinimum(field)) / 2;
            if (val.get(field) > halfwayPoint) {
                val.add(field, 1);
            }
        }
    }
}
