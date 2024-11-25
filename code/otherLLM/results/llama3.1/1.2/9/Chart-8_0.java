public Week(Date time, TimeZone zone) {
    if (time == null || zone == null) {
        throw new NullPointerException("Input parameters cannot be null");
    }
    this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
}