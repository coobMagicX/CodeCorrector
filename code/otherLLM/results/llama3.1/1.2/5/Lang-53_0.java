private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    
    if (field == Calendar.MILLISECOND) {
        return;
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    // see http://issues.apache.org/jira/browse/LANG-59
    //
    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods.

    Date date = val.getTime();
    long time = date.getTime();
    boolean done = false;

    // truncate milliseconds
    int millisecs = val.get(Calendar.MILLISECOND);
    if (!round || millisecs < 500) {
        time = time - millisecs;
    }
    
    // truncate seconds and milliseconds in one go
    if (field == Calendar.SECOND) {
        done = true;
        long seconds = val.get(Calendar.SECOND);
        int remainingMsec = (int)((time % 1000L));
        if (!round || seconds < 30 + (remainingMsec / 1000)) {
            time -= (seconds * 1000L) - remainingMsec;
        }
    }

    // truncate minutes and seconds in one go
    if (field == Calendar.MINUTE) {
        done = true;
        long minutes = val.get(Calendar.MINUTE);
        int remainingSecs = (int)((time % 60000L) / 1000);
        if (!round || minutes < 30 + (remainingSecs)) {
            time -= (minutes * 60000L) - (remainingSecs * 1000L);
        }
    }

    // reset time
    if (date.getTime() != time) {
        date.setTime(time);
        val.setTime(date);
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
                        //We need at add one to this field since the
                        //  last number causes us to round up
                        val.add(fields[i][0], 1);
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
                    //If we're going to drop the DATE field's value,
                    //  we want to do this our own way.
                    offset = val.get(Calendar.DATE);
                    if (offset >= 16) {
                        offset -= 15;
                    }
                    roundUp = offset > 7;
                    offsetSet = true;
                }
                break;
            case Calendar.AM_PM:
                if (fields[i][0] == Calendar.HOUR_OF_DAY) {
                    //If we're going to drop the HOUR field's value,
                    //  we want to do this our own way.
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
            //Calculate the offset from the minimum allowed value
            offset = val.get(fields[i][0]) - min;
            //Set roundUp if this is more than half way between the minimum and maximum
            roundUp = offset > ((max - min) / 2);
        }
        //We need to remove this field
        if (offset != 0) {
            val.set(fields[i][0], val.get(fields[i][0]) - offset);
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");

}