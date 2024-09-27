public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    // Convert both calendars to GMT timezone for consistent comparison
    TimeZone gmt = TimeZone.getTimeZone("GMT");
    Calendar cal1GMT = (Calendar) cal1.clone();
    Calendar cal2GMT = (Calendar) cal2.clone();
    cal1GMT.setTimeZone(gmt);
    cal2GMT.setTimeZone(gmt);

    return (cal1GMT.get(Calendar.MILLISECOND) == cal2GMT.get(Calendar.MILLISECOND) &&
            cal1GMT.get(Calendar.SECOND) == cal2GMT.get(Calendar.SECOND) &&
            cal1GMT.get(Calendar.MINUTE) == cal2GMT.get(Calendar.MINUTE) &&
            cal1GMT.get(Calendar.HOUR_OF_DAY) == cal2GMT.get(Calendar.HOUR_OF_DAY) &&
            cal1GMT.get(Calendar.DAY_OF_YEAR) == cal2GMT.get(Calendar.DAY_OF_YEAR) &&
            cal1GMT.get(Calendar.YEAR) == cal2GMT.get(Calendar.YEAR) &&
            cal1GMT.get(Calendar.ERA) == cal2GMT.get(Calendar.ERA));
}