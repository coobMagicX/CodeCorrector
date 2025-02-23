private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // --- Fix for LANG-59 --- START ---
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }
    if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }
    // --- Fix for LANG-59 --- END ---

    boolean roundUp = false;
    int offset;
    boolean offsetSet;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                if (round && roundUp) {
                    val.add(fields[i][0], 1);
                }
                return;
            }
        }
        offset = 0;
        offsetSet = false;
        switch (field) {
            case DateUtils.SEMI_MONTH:
                if (fields[i][0] == Calendar.DATE) {
                    offset = val.get(Calendar.DATE) - 1;
                    if (offset >= 15) {
                        offset -= 15;
                    }
                    roundUp = offset > 7;
                    offsetSet = true;
                }
                break;
            case Calendar.AM_PM:
                if (fields[i][0] == Calendar.HOUR_OF_DAY) {
                    offset = val.get(Calendar.HOUR_OF_DAY);
                    if (offset >= 12) {
                        offset -= 12;
                    }
                    roundUp = offset > 6;
                    offsetSet = true;
                }
                break;
        }
        if (!offsetSet) {
            int min = val.getActualMinimum(fields[i][0]);
            int max = val.getActualMaximum(fields[i][0]);
            offset = val.get(fields[i][0]) - min;
            roundUp = offset > ((max - min) / 2);
        }
        val.set(fields[i][0], val.get(fields[i][0]) - offset);
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}
