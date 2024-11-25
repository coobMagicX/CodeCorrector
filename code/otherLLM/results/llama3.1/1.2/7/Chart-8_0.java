public Week(Date time, TimeZone zone) {
    if (time == null || zone == null) {
        throw new IllegalArgumentException("Both time and zone must be non-null");
    }
    
    this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
}