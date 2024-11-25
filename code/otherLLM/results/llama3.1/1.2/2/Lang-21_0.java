public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }
    return (cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
            cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
            cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) &&
            cal1.getTimeZone().getID().equals(cal2.getTimeZone().getID()));
}