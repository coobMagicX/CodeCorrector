private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    // Manually truncate milliseconds, seconds and minutes, rather than using Calendar methods.

    boolean roundUp = false;

    // Truncate milliseconds
    val.set(Calendar.MILLISECOND, 0);

    // Truncate seconds
    if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
        roundUp = val.get(Calendar.MINUTE) > 30;
    } else if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
        roundUp = val.get(Calendar.HOUR_OF_DAY) > 12; // Afternoon/evening
    }

    // Truncate minutes
    if (field == Calendar.HOUR || field == Calendar.HOUR_OF_DAY) {
        val.set(Calendar.HOUR_OF_DAY, 0);
        roundUp = val.get(Calendar.DAY_OF_MONTH) > 15; // After mid-month
    } else if (field == Calendar.DAY_OF_WEEK || field == Calendar.DAY_OF_MONTH) {
        val.set(Calendar.DAY_OF_MONTH, 1);
        int weekDay = val.get(Calendar.DAY_OF_WEEK);
        roundUp = weekDay > 4; // After Thursday
    } else if (field == Calendar.MONTH) {
        val.set(Calendar.MONTH, val.getActualMinimum(Calendar.MONTH));
        roundUp = false; // Starting at the first month
    } else if (field == Calendar.YEAR) {
        val.clear(); // Clear all fields and set to January 1st of the same year
    }

    // Adjust for rounding up as necessary
    if (round && roundUp) {
        switch (field) {
            case Calendar.SECOND:
                val.add(Calendar.MINUTE, 1);
                break;
            case Calendar.MINUTE:
                val.add(Calendar.HOUR_OF_DAY, 1);
                break;
            case Calendar.HOUR_OF_DAY:
                val.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case Calendar.DAY_OF_WEEK:
                val.add(Calendar.DAY_OF_MONTH, 7);
                break;
            case Calendar.DAY_OF_MONTH:
                val.add(Calendar.MONTH, 1);
                break;
            case Calendar.MONTH:
                if (val.getActualMaximum(Calendar.YEAR) == val.get(Calendar.YEAR)) {
                    // If it's the last month of the year, increment the year
                    val.add(Calendar.YEAR, 1);
                } else {
                    val.add(Calendar.MONTH, 1);
                }
                break;
            case Calendar.YEAR:
                // Year rounding is handled by clearing all fields and setting to January 1st
                break;
        }
    }

    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    // Additional field handling if necessary
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                // This is our field... we stop looping
                if (round && roundUp) {
                    if (field == DateUtils.SEMI_MONTH) {
                        if (val.get(Calendar.DATE) == 1) {
                            val.add(Calendar.MONTH, 15);
                        } else {
                            val.add(Calendar.MONTH, -15);
                            int year = val.get(Calendar.YEAR);
                            val.set(Calendar.MONTH, Calendar.DECEMBER);
                            if (val.getActualMaximum(Calendar.YEAR) != year) {
                                val.add(Calendar.YEAR, -1);
                            }
                        }
                    } else {
                        // We need to add one to this field since the last number causes us to round up
                        val.add(fields[i][0], 1);
                    }
                }
                return;
            }
        }
    }

    throw new IllegalArgumentException("The field " + field + " is not supported");
}