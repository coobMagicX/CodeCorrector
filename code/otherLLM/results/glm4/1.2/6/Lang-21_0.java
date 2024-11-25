public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    
    // Ensure both Calendars are set to the same time zone and ignore DST before comparison.
    TimeZone tz = cal1.getTimeZone();
    cal1.setTimeZone(TimeZone.getDefault());
    cal2.setTimeZone(TimeZone.getDefault());

    return (cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) &&
            cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
            cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
            cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) && // Hour in Calendar is HOUR_OF_DAY
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA));
}

// Adjusted the comparison of HOUR to HOUR_OF_DAY since Calendar.HOUR is ambiguous (12-hour clock).