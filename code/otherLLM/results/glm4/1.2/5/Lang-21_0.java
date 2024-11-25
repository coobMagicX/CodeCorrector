public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }
    
    // Check if the calendars are in the same time zone and the date-time values match
    return (cal1.get(Calendar.ZONE_OFFSET) == cal2.get(Calendar.ZONE_OFFSET) &&
            cal1.get(Calendar.DSTOFFSET) == cal2.get(Calendar.DSTOFFSET) &&
            cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) &&
            cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
            cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
            cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.getClass() == cal2.getClass());
}