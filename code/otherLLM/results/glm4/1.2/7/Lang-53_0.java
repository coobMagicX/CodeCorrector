public void modify(Calendar val, int field, boolean round) {
    // Check for year overflow
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Skip milliseconds modification
    if (field == Calendar.MILLISECOND) {
        return;
    }

    // Manual truncation for lower fields
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

    if (!done && (!round || val.get(Calendar.SECOND) < 30)) {
        time -= val.get(Calendar.SECOND) * 1000L;
        if (field == Calendar.MINUTE) {
            done = true;
        }
    }

    if (!done && (!round || val.get(Calendar.MINUTE) < 30)) {
        time -= val.get(Calendar.MINUTE) * 60000L;
    }

    // Reset the calendar's time
    if (date.getTime() != time) {
        date.setTime(time);
        val.setTime(date);
    }

    // Rounding logic
    boolean roundUp = false;

    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                if (round && roundUp) {
                    if (field == DateUtils.SEMI_MONTH) {
                        int day = val.get(Calendar.DATE);
                        if (day == 1) {
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

        // Special handling for semi-month and AM/PM
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

        // Standard rounding
        if (!offsetSet) {
            int min = val.getActualMinimum(fields[i][0]);
            int max = val.getActualMaximum(fields[i][0]);
            offset = val.get(fields[i][0]) - min;
            roundUp = offset > ((max - min) / 2);
        }

        if (round && offsetSet) {
            val.add(fields[i][0], offset);
        }
    }

    // Unsupported field error
    throw new IllegalArgumentException("Unsupported field: " + field);
}