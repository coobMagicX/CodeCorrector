public Week(Date time, TimeZone zone) {
    // defer argument checking...
    this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, getFirstDayOfWeek(Locale.getDefault()));
}

// Assuming this method is available...
private int getFirstDayOfWeek(Locale locale) {
    if (locale.getCountry().equals("US")) {
        return 0; // Sunday
    } else {
        return 1; // Monday
    }
}