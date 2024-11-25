public StringBuffer format(Calendar calendar, StringBuffer buf) {
    // Check if the time zone should be overridden and the current buffer is not already in UTC
    if (mTimeZoneForced && !calendar.getTimeZone().equals(TimeZone.getTimeZone("UTC"))) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    return applyRules(calendar, buf);
}