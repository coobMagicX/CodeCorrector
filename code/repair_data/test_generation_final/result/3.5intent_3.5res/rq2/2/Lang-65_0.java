private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // truncate milliseconds
    val.set(Calendar.MILLISECOND, 0);

    // truncate seconds
    val.set(Calendar.SECOND, 0);

    // truncate minutes
    val.set(Calendar.MINUTE, 0);

    // reset time
    val.set(Calendar.HOUR_OF_DAY, 0);
    val.set(Calendar.HOUR, 0);
    val.set(Calendar.AM_PM, 0);

    boolean roundUp = false;
    boolean fieldSupported = false; // Added variable to check if the field is supported
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                // This is our field... we stop looping
                if (round && roundUp) {
                    if (field == DateUtils.SEMI_MONTH) {
                        // This is a special case that's hard to generalize
                        // If the date is 1, we round up to 16, otherwise
                        // we subtract 15 days and add 1 month
                        if (val.get(Calendar.DATE) == 1) {
                            val.add(Calendar.DATE, 15);
                        } else {
                            val.add(Calendar.DATE, -15);
                            val.add(Calendar.MONTH, 1);
                        }
                    } else {
                        // We need to add one to this field since the
                        // last number causes us to round up
                        val.add(fields[i][0], 1);
                    }
                }
                fieldSupported = true; // The field is supported
                break; // Added break statement to stop looping
            }
        }
        if (fieldSupported) {
            break; // Added break statement to stop looping
        }
        // We have various fields that are not easy roundings
        int offset = 0;
        boolean offsetSet = false;
        // These are special types of fields that require different rounding rules
        switch (field) {
            case DateUtils.SEMI_MONTH:
                if (fields[i][0] == Calendar.DATE) {
                    // If we're going to drop the DATE field's value,
                    // we want to do this our own way.
                    // We need to subtract 1 since the date has a minimum of 1
                    offset = val.get(Calendar.DATE) - 1;
                    // If we're above 15 days adjustment, that means we're in the
                    // bottom half of the month and should stay accordingly.
                    if (offset >= 15) {
                        offset -= 15;
                    }
                    // Record whether we're in the top or bottom half of that range
                    roundUp = offset > 7;
                    offsetSet = true;
                }
                break;
            case Calendar.AM_PM:
                if (fields[i][0] == Calendar.HOUR_OF_DAY) {
                    // If we're going to drop the HOUR field's value,
                    // we want to do this our own way.
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
            // Calculate the offset from the minimum allowed value
            offset = val.get(fields[i][0]) - min;
            // Set roundUp if this is more than half way between the minimum and maximum
            roundUp = offset > ((max - min) / 2);
        }
        // We need to remove this field
        val.set(fields[i][0], val.get(fields[i][0]) - offset);
    }
    if (!fieldSupported) { // Added condition to check if the field is supported
        throw new IllegalArgumentException("The field " + field + " is not supported");
    }
}