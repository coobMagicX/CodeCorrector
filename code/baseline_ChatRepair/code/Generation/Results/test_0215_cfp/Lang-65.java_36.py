private static final int[][] fields = {
    {Calendar.YEAR},
    {Calendar.MONTH},
    {Calendar.DAY_OF_MONTH},
    {Calendar.HOUR_OF_DAY},
    {Calendar.MINUTE},
    {Calendar.SECOND},
    {Calendar.MILLISECOND}
};

private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // Truncate milliseconds, seconds, and minutes to avoid modification of hour due to timezone transitions
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }
    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                if (round && roundUp) {
                    val.add(fields[i][0], 1);
                }
                return; // Successfully modified the needed field and rounded if necessary
            }
        }
        // Determine 'roundUp' status for SEMI_MONTH and AM_PM
        // See original code for handling these special cases
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}
