private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    //
    // Manually truncate milliseconds, seconds, and minutes, rather than using
    // Calendar methods.

    // truncate milliseconds
    if (field == Calendar.SECOND || field == Calendar.MINUTE || field == Calendar.HOUR_OF_DAY || field == Calendar.AM_PM) {
        val.set(Calendar.MILLISECOND, 0);
    }

    // truncate seconds
    if (field == Calendar.MINUTE || field == Calendar.HOUR_OF_DAY || field == Calendar.AM_PM) {
        val.set(Calendar.SECOND, 0);
    }

    // truncate minutes
    if (field == Calendar.HOUR_OF_DAY || field == Calendar.AM_PM) {
        val.set(Calendar.MINUTE, 0);
    }

    // reset time if needed to account for crossing into a different day/month/year due to truncation
    if (field == Calendar.HOUR_OF_DAY && val.get(Calendar.HOUR_OF_DAY) == 0) {
        if (round && val.getActualMaximum(Calendar.HOUR_OF_DAY) > 12) {
            // Assuming rounding mode rounds to next day if past mid-day
            val.add(Calendar.DATE, 1);
        }
    }
    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    int[] relevantFields = {
        Calendar.MILLISECOND,
        Calendar.SECOND,
        Calendar.MINUTE,
        Calendar.HOUR_OF_DAY,
        Calendar.DATE,
        Calendar.MONTH,
        Calendar.YEAR
    };
    for (int i = 0; i < relevantFields.length; i++) {
        if (relevantFields[i] == field) {
            if (round && roundUp) {
                val.add(field, 1);
            }
            return;
        }
        int offset = val.get(relevantFields[i]);
        if (offset > 0) {
            roundUp = offset >= (val.getActualMaximum(relevantFields[i]) - val.getActualMinimum(relevantFields[i])) / 2;
            val.set(relevantFields[i], val.getActualMinimum(relevantFields[i]));
        }
    }

    throw new IllegalArgumentException("The field " + field + " is not supported");
}
