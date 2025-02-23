private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Manual truncation for milliseconds, seconds, minutes as required
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
    if (field == Calendar.HOUR_OF_DAY) {
        val.set(Calendar.HOUR_OF_DAY, val.get(Calendar.HOUR_OF_DAY));
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }

    boolean roundUp = false;
    int[][] fields = {
        {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND}
    };
    
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            int innerField = fields[i][j];
            if (innerField == field) {
                if (round) {
                    int offset = val.get(innerField) - val.getActualMinimum(innerField);
                    roundUp = offset > ((val.getActualMaximum(innerField) - val.getActualMinimum(innerField)) / 2);
                    if (roundUp) {
                        val.add(innerField, 1);
                    }
                }
                return;
            }
            val.set(innerField, val.getActualMinimum(innerField));
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}
