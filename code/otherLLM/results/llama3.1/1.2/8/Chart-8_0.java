public Week(Date time, TimeZone zone) {
    // check if inputs are valid...
    if (time == null || zone == null) {
        throw new IllegalArgumentException("Invalid input arguments");
    }
    
    this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
}