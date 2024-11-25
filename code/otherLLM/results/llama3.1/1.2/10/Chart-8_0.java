public Week(Date time, TimeZone zone) {
    // defer argument checking...
    Locale locale = getLocaleFromZone(zone);
    this(time, RegularTimePeriod.DEFAULT_TIME_ZONE, locale);
}

private Locale getLocaleFromZone(TimeZone zone) {
    String zoneId = zone.getID();
    if (zoneId.startsWith("US/")) { // US timezone
        return Locale.US;
    } else if (zoneId.startsWith("GBR/")) { // UK timezone
        return Locale.UK;
    }
    // Add more locale mappings as needed...
    return Locale.getDefault();
}