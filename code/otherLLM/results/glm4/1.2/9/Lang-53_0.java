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
        if (field == Calendar.SECOND) {
            done = true;
        }
    }

    // truncate seconds
    if (!done) {
        int seconds = val.get(Calendar.SECOND);
        if (!round || seconds < 30) {
            time -= (seconds * 1000L);
            if (field == Calendar.MINUTE) {
                done = true;
            }
        }
    }

    // truncate minutes
    if (!done) {
        int minutes = val.get(Calendar.MINUTE);
        if (!round || minutes < 30) {
            time -= (minutes * 60000L);
        }
    }

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
                        if (val.get(Calendar.DATE) == 1) {
                            val.add(Calendar.DATE, 15);
                        } else {
                            val.add(Calendar.DATE, -15);
                            val.add(Calendar.MONTH, 1);
                        }
                    } else {
                        val.add(fields[i][0], 1);
                    }
                }
                return;
            }
        }

        int offset = 0;
        boolean offsetSet = false;
        switch (field) {
            case DateUtils.SEMI_MONTH:
                if (fields[i][0] == Calendar.DATE) {
                    offset = val.get(Calendar.DATE) - 1;
                    if (offset >= 15) {
                        offset -= 15;
                    }
                    roundUp = offset > 7;
                    offsetSet = true;
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
        
        if (offset != 0) {
            val.set(fields[i][0], val.get(fields[i][0]) - offset);
        }
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}