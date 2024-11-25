private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods.

    val.set(Calendar.MILLISECOND, 0); // Truncate milliseconds to zero

    if (round) {
        int currentSecond = val.get(Calendar.SECOND);
        val.set(Calendar.SECOND, currentSecond - (currentSecond % 1)); // Truncate seconds
        val.set(Calendar.MINUTE, val.getActualMinimum(Calendar.MINUTE)); // Set minutes to minimum value to truncate them as well
    } else {
        val.set(Calendar.SECOND, 0); // Truncate seconds to zero if not rounding
        val.set(Calendar.MINUTE, 0); // Reset minutes to zero
    }
    
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
                        //We need to add one to this field since the last number causes us to round up
                        val.add(fields[i][0], 1);
                    }
                }
                return;
            }
        }
    }

    throw new IllegalArgumentException("The field " + field + " is not supported");
}