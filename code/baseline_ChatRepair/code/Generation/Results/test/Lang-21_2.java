public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The date must not be null");
    }

    
    Calendar normalizedCal1 = (Calendar) cal1.clone();
    Calendar normalizedCal2 = (Calendar) cal2.clone();

    
    normalizedCal1.setTimeZone(TimeZone.getTimeZone("UTC"));
    normalizedCal2.setTimeZone(TimeZone.getTimeZone("UTC"));
    
    return (normalizedCal1.get(Calendar.MILLISECOND) == normalizedCal2.get(Calendar.MILLISECOND) &&
            normalizedCal1.get(Calendar.SECOND) == normalizedCal2.get(Calendar.SECOND) &&
            normalizedCal1.get(Calendar.MINUTE) == normalizedCal2.get(Calendar.MINUTE) &&
            normalizedCal1.get(Calendar.HOUR_OF_DAY) == normalizedCal2.get(Calendar.HOUR_OF_DAY) &&
            normalizedCal1.get(Calendar.DAY_OF_YEAR) == normalizedCal2.get(Calendar.DAY_OF_YEAR) &&
            normalizedCal1.get(Calendar.YEAR) == normalizedCal2.get