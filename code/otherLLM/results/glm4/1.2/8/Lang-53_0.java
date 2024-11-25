import java.util.Calendar;

public void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    
    if (field == Calendar.MILLISECOND) {
        return;
    }

    Date date = val.getTime();
    long time = date.getTime();
    boolean done = false;

    int millisecs = val.get(Calendar.MILLISECOND);
    if (!round || millisecs < 500) {
        time -= millisecs;
        if (field == Calendar.SECOND) {
            done = true;
        }
    }

    int seconds = val.get(Calendar.SECOND);
    if (!done && (!round || seconds < 30)) {
        time -= seconds * 1000L;
        if (field == Calendar.MINUTE) {
            done = true;
        }
    }

    int minutes = val.get(Calendar.MINUTE);
    if (!done && (!round || minutes < 30)) {
        time -= minutes * 60000L;
    }

    if (date.getTime() != time) {
        date.setTime(time);
        val.setTime(date);
    }

    boolean roundUp = false;
    for (int[] fieldInfo : fields) { // Assuming 'fields' is an array of int arrays
        if (fieldInfo[1] == field) {
            if (round && roundUp) {
                if (field == DateUtils.SEMI_MONTH) {
                    if (val.get(Calendar.DATE) == 1) {
                        val.add(Calendar.DATE, 15);
                    } else {
                        val.add(Calendar.DATE, -val.get(Calendar.DATE));
                        val.add(Calendar.MONTH, 1);
                    }
                } else {
                    val.add(fieldInfo[0], 1); // fieldInfo[0] should be the Calendar field constant
                }
            }
            return;
        }

        int offset = 0;
        boolean offsetSet = false;

        switch (field) {
            case DateUtils.SEMI_MONTH:
                if (fieldInfo[0] == Calendar.DATE) {
                    offset = val.get(Calendar.DATE) - 1;
                    if (offset >= 15) {
                        offset -= 15;
                    }
                    roundUp = offset > 7;
                    offsetSet = true;
                }
                break;
            case Calendar.AM_PM:
                if (fieldInfo[0] == Calendar.HOUR_OF_DAY) {
                    offset = val.get(Calendar.HOUR_OF_DAY) % 12;
                    roundUp = offset < 6; // Assuming the rounding rule is to round up for AM to PM or vice versa
                    offsetSet = true;
                }
                break;
        }

        if (offsetSet && round) {
            val.add(fieldInfo[0], offset);
        }
    }

    throw new IllegalArgumentException("The field " + field + " does not support rounding.");
}