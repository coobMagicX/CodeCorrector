private static final int[][] fields = {
    {Calendar.MILLISECOND},
    {Calendar.SECOND},
    {Calendar.MINUTE},
    {Calendar.HOUR_OF_DAY},
    {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM 
        /* considering all DATE-related fields need similar rounding */},
    {Calendar.MONTH, DateUtils.SEMI_MONTH},
    {Calendar.YEAR},
    {Calendar.ERA}
};

private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                // Process rounding here if 'round' is true
                if (round && roundUp) {
                    val.add(fields[i][0], 1);
                }
                return;
            }
        }

        // Truncate larger fields
        if (field == fields[i][0]) {
            truncate(val, i);
            break;
        }

    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}

private static void truncate(Calendar val, int index) {
    for (int i = 0; i <= index; i++) {
        if (fields[i].length > 1) {
            for (int field : fields[i]) {
                val.set(field, val.getActualMinimum(field));
            }
        } else {
            val.set(fields[i][0], val.getActualMinimum(fields[i][0]));
        }
    }
}
