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
    long timeInMillis = val.getTimeInMillis();
    long truncatedMillis = timeInMillis - (timeInMillis % 1000);
    val.setTime(new Date(truncatedMillis));
    
    // truncate seconds
    truncatedMillis = timeInMillis - (truncatedMillis % 60000);
    val.setTime(new Date(truncatedMillis));
    
    // truncate minutes
    truncatedMillis = timeInMillis - (truncatedMillis % 3600000);
    val.setTime(new Date(truncatedMillis));

    // reset time
    val.set(Calendar.HOUR_OF_DAY, 0);
    val.set(Calendar.MINUTE, 0);
    val.set(Calendar.SECOND, 0);
    val.set(Calendar.MILLISECOND, 0);

    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                //This is our field... we stop looping
                if (round && roundUp) {
                    // The logic to round up is already in the loop above, so no need to duplicate it.
                    return;
                }
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
                    offset = val.get(Calendar.DATE) - 1;
                    //Record whether we're in the top or bottom half of that range
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
        val.set(fields[i][0], val.get(fields[i][0]) - offset);
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}