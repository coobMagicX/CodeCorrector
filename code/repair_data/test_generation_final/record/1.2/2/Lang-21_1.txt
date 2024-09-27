public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }

    TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    Calendar cal1Gmt = (Calendar) cal1.clone();
    cal1Gmt.setTimeZone(gmtTimeZone);

    Calendar cal2Gmt = (Calendar) cal2.clone();
    cal2Gmt.setTimeZone(gmtTimeZone);

    return (cal1Gmt.get(Calendar.MILLISECOND) == cal2Gmt.get(Calendar.MILLISECOND) &&
            cal1Gmt.get(Calendar.SECOND) == cal2Gmt.get(Calendar.SECOND) &&
            cal1Gmt.get(Calendar.MINUTE) == cal2Gmt.get(Calendar.MINUTE) &&
            cal1Gmt.get(Calendar.HOUR_OF_DAY) == cal2Gmt.get(Calendar.HOUR_OF_DAY) &&
            cal1Gmt.get(Calendar.DAY_OF_YEAR) == cal2Gmt.get(Calendar.DAY_OF_YEAR) &&
            cal1Gmt.get(Calendar.YEAR) == cal2Gmt.get(Calendar.YEAR) &&
            cal1Gmt.get(Calendar.ERA) == cal2Gmt.get(Calendar.ERA));
}