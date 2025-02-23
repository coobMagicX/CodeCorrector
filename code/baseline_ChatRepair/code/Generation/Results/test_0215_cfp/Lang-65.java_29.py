private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    val.setLenient(false); // Set the leniency to false to strictly interpret the calendar fields
    int[] downFields = {
        Calendar.MILLISECOND,
        Calendar.SECOND,
        Calendar.MINUTE,
        Calendar.HOUR_OF_DAY,
        Calendar.AM_PM,
        Calendar.DATE,
        Calendar.MONTH
    };

    // Find the first relevant field below the specified field for truncation.
    for (int i = 0; i < downFields.length; i++) {
        int currentField = downFields[i];
        
        if (currentField > field) {
            if (currentField == Calendar.HOUR_OF_DAY) {
                val.set(Calendar.HOUR_OF_DAY, val.getMinimum(Calendar.HOUR_OF_DAY));
            } else {
                val.set(currentField, val.getMinimum(currentField));
            }
        } else if (currentField == field) {
            break;
        }
    }

    // Ensuring time zone shifts like day saving are considered.
    TimeZone zone = val.getTimeZone();
    if(zone.useDaylightTime() && zone.inDaylightTime(val.getTime())) {
        val.add(Calendar.HOUR, -1);
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }
}
