public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }

    
    TimeZone localTimeZone = TimeZone.getDefault();
    Calendar localCal1 = (Calendar) cal1.clone();
    localCal1.setTimeZone(localTimeZone);
    Calendar localCal2 = (Calendar) cal2.clone();
    localCal2.setTimeZone(localTimeZone);

    return (localCal1.get(Calendar.HOUR_OF_DAY) == localCal2.get(Calendar.HOUR_OF_DAY) &&
            localCal1.get(Calendar.MINUTE) == localCal2.get(Calendar.MINUTE) &&
            localCal1.get(Calendar.SECOND) == localCal2.get(Calendar.SECOND) &&
            localCal1.get(Calendar.MILLISECOND) == localCal2.get(Calendar.MILLISECOND));
}
