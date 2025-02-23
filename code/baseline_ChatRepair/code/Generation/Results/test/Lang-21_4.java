public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    if (cal1 == null || cal2 == null) {
        throw new IllegalArgumentException("The calendar objects must not be null");
    }
    
    
    long timeDifferenceMillis = cal1.getTimeInMillis() - cal2.getTimeInMillis();
    
    
    int timezoneOffsetDifference = cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET) -
                                   cal2.get(Calendar.ZONE_OFFSET) - cal2.get(Calendar.DST_OFFSET);
                                   
    
    long effectiveDifferenceMillis = timeDifferenceMillis - timezoneOffsetDifference;
    
    
    