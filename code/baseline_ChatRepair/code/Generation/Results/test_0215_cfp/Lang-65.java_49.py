private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Truncate milliseconds, seconds, and minutes
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }

    if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }

    if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }

    // Assume 'fields' is a defined array containing field levels for adjustment
    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                // Code for rounding logic here..
                // Assuming current block contains valid missing logic for handling your specific rounding
                if (round && roundUp) {
                    if (field == DateUtils.SEMI_MONTH) {
                        if (val.get(Calendar.DATE) == 1) {
                            val.add(Calendar.DATE, 15);
                        } else {
                            val.add(Calendar.DATE, -15);
                            val.add(Calendar.MONTH, 1);
                        }
                    } else {
                        val.add(fields[i][0], 1);
                    }
                }
                return;
            }
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}
