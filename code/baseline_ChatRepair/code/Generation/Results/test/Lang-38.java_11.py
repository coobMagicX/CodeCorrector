public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }

    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}
