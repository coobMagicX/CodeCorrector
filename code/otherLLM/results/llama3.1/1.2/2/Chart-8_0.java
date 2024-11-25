public Week(Date time, TimeZone zone) {
    if (zone == null || !(zone instanceof TimeZone)) {
        throw new IllegalArgumentException("Invalid time zone");
    }
    this(time, zone, Locale.getDefault());
}