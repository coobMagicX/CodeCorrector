public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null) {
        throw new IllegalArgumentException("Calendar object cannot be null.");
    }
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone(); // Clone to avoid side effects
        calendar.setTimeZone(mTimeZone);       // Set to the forced time zone
    }
    return applyRules(calendar, buf);
}
