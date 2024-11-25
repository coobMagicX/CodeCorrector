public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    
    // Set the first calendar's time zone to UTC and the second one's to the system default
    cal1.setTimeZone(TimeZone.getTimeZone("UTC"));
    cal2.setTimeZone(TimeZone.getDefault());
    
    return (cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) &&
            cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
            cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
            cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) && // Use HOUR_OF_DAY instead of HOUR
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.getClass() == cal2.getClass());
}