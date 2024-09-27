public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null || buf == null) {
        throw new IllegalArgumentException("Arguments 'calendar' and 'buf' must not be null");
    }
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}