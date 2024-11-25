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
    val.set(Calendar.SECOND, 0);
    val.set(Calendar.MINUTE, 0); // Also reset to zero after truncating seconds

    // truncate minutes
    val.set(Calendar.HOUR_OF_DAY, 0); // Reset hours as well for proper minute truncation

    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                //This is our field... we stop looping
                if (round && roundUp) {
                    switch (field) {
                        case DateUtils.SEMI_MONTH:
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                            break;
                        default:
                            val.add(fields[i][0], 1); // Add one to round up
                    }
                }
                return;
            }
        }
        //We have various fields that are not easy roundings
        int offset = 0;
        boolean offsetSet = false;
        switch (field) {
            case DateUtils.SEMI_MONTH:
                if (fields[i][0] == Calendar.DATE) {
                    if (val.get(Calendar.DATE) == 1) {
                        val.add(Calendar.DATE, 15);
                    } else {
                        val.add(Calendar.DATE, -15);
                        val.add(Calendar.MONTH, 1);
                    }
                    return;
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
        //We need to remove this field
        if (roundUp && !offsetSet) {
            val.set(fields[i][0], val.get(fields[i][0]) + offset);
        } else {
            val.set(fields[i][0], val.get(fields[i][0]) - offset);
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}