public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    
    // Ensure both calendars are in the same timezone for accurate comparison.
    TimeZone tz1 = cal1.getTimeZone();
    TimeZone tz2 = cal2.getTimeZone();
    if (!tz1.equals(tz2)) {
        cal1.setTime(cal1.getTime().getTime() - getFragmentInMilliseconds(cal1, Calendar.ZONE_OFFSET) + getFragmentInMilliseconds(cal2, Calendar.ZONE_OFFSET));
    }
    
    return (cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) &&
            cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
            cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
            cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) && // Changed HOUR to HOUR_OF_DAY
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
            cal1.getClass() == cal2.getClass());
}