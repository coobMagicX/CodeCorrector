private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Manually truncate milliseconds, seconds and minutes, rather than using
    // Calendar methods.
    val.set(Calendar.MILLISECOND, 0);
    val.set(Calendar.SECOND, 0);
    val.set(Calendar.MINUTE, 0);

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
                            val = addMonths(val, 1); // Use the provided method to add months
                        }
                    } else {
                        //We need at add one to this field since the
                        //  last number causes us to round up
                        val.set(fields[i][0], val.get(fields[i][0]) + 1);
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
                    //If we're going to drop the DATE field's value,
                    //  we want to do this our own way.
                    //We need to subtrace 1 since the date has a minimum of 1
                    offset = val.get(Calendar.DATE) - 1;
                    //If we're above 15 days adjustment, that means we're in the
                    //  bottom half of the month and should stay accordingly.
                    if (offset >= 15) {
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

        val.set(fields[i][0], val.get(fields[i][0]) - offset);
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}