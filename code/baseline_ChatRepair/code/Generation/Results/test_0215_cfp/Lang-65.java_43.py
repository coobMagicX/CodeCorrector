private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // truncating milliseconds, seconds, and minutes
    if (field > Calendar.MILLISECOND) {
        val.clear(Calendar.MILLISECOND);
    }
    if (field > Calendar.SECOND) {
        val.clear(Calendar.SECOND);
    }
    if (field > Calendar.MINUTE) {
        val.clear(Calendar.MINUTE);
    }

    boolean roundUp = false;
    int[][] fields = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND},
        {Calendar.MINUTE},
        {Calendar.HOUR_OF_DAY, Calendar.HOUR},
        {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM},
        {Calendar.MONTH, DateUtils.SEMI_MONTH},
        {Calendar.YEAR},
        {Calendar.ERA}
    };

    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                if (round && roundUp) {
                    val.add(fields[i][0], 1);
                }
                return; // exit after setting field
            }
        }
    }

    throw new IllegalArgumentException("The field " + field + " is not supported");
}
