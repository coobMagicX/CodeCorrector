private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    
    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods to support very old versions of Java.

    // Truncate milliseconds
    if (field == Calendar.SECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }

    // Truncate seconds
    if (field == Calendar.MINUTE) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }

    // Truncate minutes
    if (field == Calendar.HOUR_OF_DAY || field == Calendar.HOUR) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }

    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    for (int[] fieldArray : fields) {
        for (int f : fieldArray) {
            if (f == field) {
                if (round && roundUp) {
                    if (field == DateUtils.SEMI_MONTH) {
                        if (val.get(Calendar.DATE) == 1) {
                            val.add(Calendar.DATE, 15);
                        } else {
                            val.add(Calendar.DATE, -15);
                            val.add(Calendar.MONTH, 1);
                        }
                    } else {
                        val.add(fieldArray[0], 1);
                    }
                }
                return;
            }
        }
        int offset = 0;
        boolean offsetSet = false;
        switch (field) {
            case DateUtils.SEMI_MONTH:
                if (fieldArray[0] == Calendar.DATE) {
                    offset = val.get(Calendar.DATE) - 1;
                    if (offset >= 15) {
                        offset -= 15;
                    }
                    roundUp = offset > 7;
                    offsetSet = true;
                }
                break;
            case Calendar.AM_PM:
                if (fieldArray[0] == Calendar.HOUR_OF_DAY) {
                    offset = val.get(Calendar.HOUR_OF_DAY);
                    if (offset >= 12) {
                        offset -= 12;
                    }
                    roundUp = offset >= 6;
                    offsetSet = true;
                }
                break;
        }
        if (!offsetSet) {
            int min = val.getActualMinimum(fieldArray[0]);
            int max = val.getActualMaximum(fieldArray[0]);
            offset = val.get(fieldArray[0]) - min;
            roundUp = offset > ((max - min) / 2);
        }
        val.set(fieldArray[0], val.get(fieldArray[0]) - offset);
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}
