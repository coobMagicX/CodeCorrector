private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    //

    // Manually truncate milliseconds, seconds, and minutes, rather than using Calendar methods.

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

    // reset time
    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    int[][] fields = {
        {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
         Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
         Calendar.MILLISECOND}
    };

    for (int[] fieldGroup : fields) {
        for (int f : fieldGroup) {
            if (f == field) {
                roundUp = false; // Stops unnecessary processing
                break;
            }
        }

        if (round && roundUp) {
            val.add(field, 1);
        }
    }

    throw new IllegalArgumentException("The field " + field + " is not supported");
}
