public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    // Convert both calendars to UTC for a fair comparison
    Calendar cal1UTC = (Calendar) cal1.clone();
    Calendar cal2UTC = (Calendar) cal2.clone();
    cal1UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
    cal2UTC.setTimeZone(TimeZone.getTimeZone("UTC"));

    return (cal1UTC.get(Calendar.MILLISECOND) == cal2UTC.get(Calendar.MILLISECOND) &&
            cal1UTC.get(Calendar.SECOND) == cal2UTC.get(Calendar.SECOND) &&
            cal1UTC.get(Calendar.MINUTE) == cal2UTC.get(Calendar.MINUTE) &&
            cal1UTC.get(Calendar.HOUR_OF_DAY) == cal2UTC.get(Calendar.HOUR_OF_DAY) &&
            cal1UTC.get(Calendar.DAY_OF_YEAR) == cal2UTC.get(Calendar.DAY_OF_YEAR) &&
            cal1UTC.get(Calendar.YEAR) == cal2UTC.get(Calendar.YEAR) &&
            cal1UTC.get(Calendar.ERA) == cal2UTC.get(Calendar.ERA));
}