public Week(Date time, TimeZone zone) {
    if (zone == null || !zone.getDisplayName().toLowerCase(Locale.getDefault()).contains("gmt")) {
        throw new IllegalArgumentException("Invalid time zone: " + zone);
    }
    this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
}