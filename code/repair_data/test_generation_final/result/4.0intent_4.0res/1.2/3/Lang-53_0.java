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
    } else {
        time = time + (1000 - millisecs);
    }
    if (field == Calendar.SECOND) {
        done = true;
    }

    // truncate seconds
    int seconds = val.get(Calendar.SECOND);
    if (!done) {
        if (!round || seconds < 30) {
            time = time - (seconds * 1000L);
        } else {
            time = time + ((60 - seconds) * 1000L);
        }
        if (field == Calendar.MINUTE) {
            done = true;
        }
    }

    // truncate minutes
    int minutes = val.get(Calendar.MINUTE);
    if (!done) {
        if (!round || minutes < 30) {
            time = time - (minutes * 60000L);
        } else {
            time = time + ((60 - minutes) * 60000L);
        }
    }

    // reset time
    if (date.getTime() != time) {
        date.setTime(time);
        val.setTime(date);
    }
    // ----------------- Fix for LANG-59 ----------------------- END ----------------
}