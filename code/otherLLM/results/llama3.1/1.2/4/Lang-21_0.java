public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    
    // Correctly handle daylight saving time by considering the timezone offset
    int tzOffset1 = cal1.getTimeZone().getRawOffset();
    int tzOffset2 = cal2.getTimeZone().getRawOffset();
    
    // Refine comparison logic to consider other time zone factors like DST transitions
    return (cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) &&
            cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
            cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
            cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.getTimeZone().getID().equals(cal2.getTimeZone().getID()) &&
            tzOffset1 == tzOffset2);
}