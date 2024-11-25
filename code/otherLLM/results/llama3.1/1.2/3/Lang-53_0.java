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

    // truncate milliseconds
    int millisecs = val.get(Calendar.MILLISECOND);
    if (!round || millisecs < 500) {
        time -= millisecs;
    }

    // truncate seconds
    int seconds = val.get(Calendar.SECOND);
    if (!round || seconds < 30) {
        time -= (seconds * 1000L);
    }

    // truncate minutes
    int minutes = val.get(Calendar.MINUTE);
    if (!round || minutes < 30) {
        time -= (minutes * 60000L);
    }

    // reset time
    date.setTime(time);
    val.setTime(date);

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                //This is our field... we stop looping
                if (round && roundUp) {
                    switch(field) {
                        case DateUtils.SEMI_MONTH:
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                            break;
                        default:
                            val.add(field, 1);
                    }
                }
                return;
            }
        }
    }

    int min = val.getActualMinimum(field);
    int max = val.getActualMaximum(field);

    //Calculate the offset from the minimum allowed value
    int offset = val.get(field) - min;

    //Set roundUp if this is more than half way between the minimum and maximum
    roundUp = offset > ((max - min) / 2);

    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                //We need to remove this field
                val.set(field, val.get(field) - offset);
                return;
            }
        }
    }

    throw new IllegalArgumentException("The field " + field + " is not supported");
}