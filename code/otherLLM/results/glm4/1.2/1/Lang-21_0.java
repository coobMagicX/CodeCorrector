public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    // Get the local time in milliseconds for both calendars
    long millis1 = getFragmentInDays(cal1, Calendar.DAY_OF_YEAR) * 24 * 60 * 60 * 1000L +
                   cal1.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000L +
                   cal1.get(Calendar.MINUTE) * 60 * 1000L +
                   cal1.get(Calendar.SECOND) * 1000L +
                   cal1.get(Calendar.MILLISECOND);
    long millis2 = getFragmentInDays(cal2, Calendar.DAY_OF_YEAR) * 24 * 60 * 60 * 1000L +
                   cal2.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000L +
                   cal2.get(Calendar.MINUTE) * 60 * 1000L +
                   cal2.get(Calendar.SECOND) * 1000L +
                   cal2.get(Calendar.MILLISECOND);
    
    return millis1 == millis2;
}