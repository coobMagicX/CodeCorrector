private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    //
    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods.

    // truncate milliseconds
    val.set(Calendar.MILLISECOND, 0);

    // truncate seconds
    long time = val.getTimeInMillis();
    switch (field) {
        case Calendar.SECOND:
            time -= (time % 1000);
            break;
        case Calendar.MINUTE:
            time -= ((time / 1000) % 60 * 1000);
            break;
        case Calendar.HOUR_OF_DAY:
            time -= (((time / 60000) % 24) * 3600000L);
            break;
        case Calendar.HOUR:
            // You might want to handle this differently than HOUR_OF_DAY
            time -= (((time / 3600000L) % 24) * 3600000L);
            break;
    }
    val.setTimeInMillis(time);

    // reset time
    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                //This is our field... we stop looping
                if (round && roundUp) {
                    if (field == DateUtils.SEMI_MONTH) {
                        //This is a special case that's hard to generalize
                        //If the date is 1, we round up to 16, otherwise
                        //  we subtract 15 days and add 1 month
                        if (val.get(Calendar.DATE) == 1) {
                            val.add(Calendar.DATE, 15);
                        } else {
                            val.add(Calendar.DATE, -15);
                            val.add(Calendar.MONTH, 1);
                        }
                    } else {
                        //We need at add one to this field since the
                        //  last number causes us to round up
                        val.add(fields[i][0], 1);
                    }
                }
                return;
            }
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}