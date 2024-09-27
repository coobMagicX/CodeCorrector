public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    
    TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
    
    Calendar cal1Gmt = Calendar.getInstance(gmtTimeZone);
    cal1Gmt.setTimeInMillis(cal1.getTimeInMillis());
    
    Calendar cal2Gmt = Calendar.getInstance(gmtTimeZone);
    cal2Gmt.setTimeInMillis(cal2.getTimeInMillis());
    
    return (cal1Gmt.get(Calendar.MILLISECOND) == cal2Gmt.get(Calendar.MILLISECOND) &&
            cal1Gmt.get(Calendar.SECOND) == cal2Gmt.get(Calendar.SECOND) &&
            cal1Gmt.get(Calendar.MINUTE) == cal2Gmt.get(Calendar.MINUTE) &&
            cal1Gmt.get(Calendar.HOUR_OF_DAY) == cal2Gmt.get(Calendar.HOUR_OF_DAY) &&
            cal1Gmt.get(Calendar.DAY_OF_YEAR) == cal2Gmt.get(Calendar.DAY_OF_YEAR) &&
            cal1Gmt.get(Calendar.YEAR) == cal2Gmt.get(Calendar.YEAR) &&
            cal1Gmt.get(Calendar.ERA) == cal2Gmt.get(Calendar.ERA) &&
            cal1Gmt.getClass() == cal2Gmt.getClass());
}