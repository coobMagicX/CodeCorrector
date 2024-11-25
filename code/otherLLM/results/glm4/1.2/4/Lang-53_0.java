private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    
    if (field == Calendar.MILLISECOND) {
        return;
    }

    Date date = val.getTime();
    long time = date.getTime();
    boolean done = false;

    // truncate milliseconds
    int millisecs = val.get(Calendar.MILLISECOND);
    if (!round || millisecs < 500) {
        time -= millisecs;
    }
    
    if (field == Calendar.SECOND && !done) {
        done = true;
    }

    // truncate seconds
    int seconds = val.get(Calendar.SECOND);
    if (!done && (!round || seconds < 30)) {
        time -= seconds * 1000L;
    }
    
    if (field == Calendar.MINUTE && !done) {
        done = true;
    }

    // truncate minutes
    int minutes = val.get(Calendar.MINUTE);
    if (!done && (!round || minutes < 30)) {
        time -= minutes * 60000L;
    }

    // reset time
    if (date.getTime() != time) {
        date.setTime(time);
        val.setTime(date);
    }
    
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
                        //We need to add one to this field since the
                        //  last number causes us to round up
                        val.add(fields[i][0], 1);
                    }
                }
                return;
            }
        }
    }

    int offset = 0;
    boolean offsetSet = false;
    
    switch (field) {
        case DateUtils.SEMI_MONTH:
            if (fields[0][0] == Calendar.DATE) {
                //If we're going to drop the DATE field's value,
                //  we want to do this our own way.
                offset = val.get(Calendar.DATE) - 1;
                if (offset >= 15) {
                    offset -= 15;
                }
                roundUp = offset > 7;
                offsetSet = true;
            }
            break;
        case Calendar.AM_PM:
            if (fields[0][0] == Calendar.HOUR_OF_DAY) {
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
        int min = val.getActualMinimum(fields[0][0]);
        int max = val.getActualMaximum(fields[0][0]);
        offset = val.get(fields[0][0]) - min;
        roundUp = offset > ((max - min) / 2);
    }

    if (offset != 0) {
        val.set(fields[0][0], val.get(fields[0][0]) - offset);
    }
}