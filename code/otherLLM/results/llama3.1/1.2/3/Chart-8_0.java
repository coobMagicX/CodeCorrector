public Week(Date time, TimeZone zone) {
    // defer argument checking...
    this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
}

// modified to match the signature of public Week(Date time, TimeZone zone, Locale locale)