public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }

    long timeInMills = getMillis(cal1);
    long otherTimeInMills = getMillis(cal2);

    return timeInMills == otherTimeInMills;
}

private static long getMillis(Calendar cal) {
    long millisPerDay = MILLIS_PER_DAY;
    long result = 0;

    for (int field : new int[] {
        Calendar.YEAR,
        Calendar.MONTH,
        Calendar.DAY_OF_YEAR
    }) {
        if (cal.get(field) > 0) {
            result += cal.get(field) * millisPerDay;
            millisPerDay /= 24; // hours in a day
            millisPerDay /= 60; // minutes in an hour
            millisPerDay /= 60; // seconds in a minute
        }
    }

    for (int field : new int[] {
        Calendar.HOUR_OF_DAY,
        Calendar.MINUTE,
        Calendar.SECOND,
        Calendar.MILLISECOND
    }) {
        result += cal.get(field) * getMillisPerUnit(field);
    }

    return result;
}

private static long getMillisPerUnit(int unit) {
    long result = Long.MAX_VALUE;
    switch (unit) {
        case Calendar.DAY_OF_YEAR:
        case Calendar.DATE:
            result = MILLIS_PER_DAY;
            break;
        case Calendar.HOUR_OF_DAY:
            result = MILLIS_PER_HOUR;
            break;
        case Calendar.MINUTE:
            result = MILLIS_PER_MINUTE;
            break;
        case Calendar.SECOND:
            result = MILLIS_PER_SECOND;
            break;
        case Calendar.MILLISECOND:
            result = 1;
            break;
        default: throw new IllegalArgumentException("The unit " + unit + " cannot be represented in milliseconds");
    }
    return result;
}