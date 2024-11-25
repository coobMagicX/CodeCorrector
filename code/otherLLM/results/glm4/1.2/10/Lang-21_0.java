public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    
    // Normalize both Calendars to the current system's timezone before comparison
    Calendar cal1Normalized = (Calendar) cal1.clone();
    cal1Normalized.setTimeZone(cal2.getTimeZone());
    Calendar cal2Normalized = (Calendar) cal2.clone();
    cal2Normalized.setTimeZone(cal1.getTimeZone());

    return (cal1Normalized.get(Calendar.MILLISECOND) == cal2Normalized.get(Calendar.MILLISECOND) &&
            cal1Normalized.get(Calendar.SECOND) == cal2Normalized.get(Calendar.SECOND) &&
            cal1Normalized.get(Calendar.MINUTE) == cal2Normalized.get(Calendar.MINUTE) &&
            cal1Normalized.get(Calendar.HOUR_OF_DAY) == cal2Normalized.get(Calendar.HOUR_OF_DAY) &&
            cal1Normalized.get(Calendar.DAY_OF_MONTH) == cal2Normalized.get(Calendar.DAY_OF_MONTH) &&
            cal1Normalized.get(Calendar.MONTH) == cal2Normalized.get(Calendar.MONTH) &&
            cal1Normalized.get(Calendar.YEAR) == cal2Normalized.get(Calendar.YEAR) &&
            cal1Normalized.get(Calendar.ERA) == cal2Normalized.get(Calendar.ERA));
}