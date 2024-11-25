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

    // truncate seconds

    // truncate minutes

    // reset time
    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                //This is our field... we stop looping
                int min = val.getActualMinimum(fields[i][0]);
                int max = val.getActualMaximum(fields[i][0]);

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
                        if (val.get(fields[i][0]) == max) {
                            val.set(fields[i][0], max - 1);
                        } else {
                            val.add(fields[i][0], 1);
                        }
                    }
                } else {
                    int offset = val.get(fields[i][0]) - min;
                    roundUp = offset > ((max - min) / 2);

                    if (round && roundUp) {
                        if (val.get(fields[i][0]) == max) {
                            val.set(fields[i][0], max - 1);
                        } else {
                            val.add(fields[i][0], 1);
                        }
                    } else {
                        int adjust = min + ((max - min) / 2);
                        val.set(fields[i][0], Math.max(min, Math.min(max, val.get(fields[i][0]) - offset)));
                    }
                }

                return;
            }
        }
        //We have various fields that are not easy roundings
        int offset = 0;
        boolean offsetSet = false;
        //These are special types of fields that require different rounding rules
        switch (field) {
            case DateUtils.SEMI_MONTH:
                if (fields[i][0] == Calendar.DATE) {
                    //If we're going to drop the HOUR field's value,
                    //  we want to do this our own way.
                    offset = val.get(Calendar.DATE);
                    roundUp = offset > 15;
                    offsetSet = true;
                }
                break;
            case Calendar.AM_PM:
                if (fields[i][0] == Calendar.HOUR_OF_DAY) {
                    //If we're going to drop the HOUR field's value,
                    //  we want to do this our own way.
                    offset = val.get(Calendar.HOUR_OF_DAY);
                    roundUp = offset > 11;
                    offsetSet = true;
                }
                break;
        }
        if (!offsetSet) {
            int min = val.getActualMinimum(fields[i][0]);
            int max = val.getActualMaximum(fields[i][0]);
            //Calculate the offset from the minimum allowed value
            offset = val.get(fields[i][0]) - min;
            //Set roundUp if this is more than half way between the minimum and maximum
            roundUp = offset > ((max - min) / 2);
        }
        //We need to remove this field
            int adjust = min + ((max - min) / 2);
            val.set(fields[i][0], Math.max(min, Math.min(max, val.get(fields[i][0]) - offset)));
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");

}