private static final int[][] fields = {
    {Calendar.MILLISECOND},
    {Calendar.SECOND},
    {Calendar.MINUTE},
    {Calendar.HOUR_OF_DAY},
    {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM},
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
                // Since this is our field, you may process rounding here if 'round' is true
                if (round && roundUp) {
                    val.add(fields[i][j], 1);
                }
                return;
            }
        }

        if (i < index) {
            continue;
        }

        int minField = fields[i][0];
        int actualMinimum = val.getActualMinimum(minField);
        val.set(minField, actualMinimum);
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}
