public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    } else {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    return applyRules(calendar, buf);
}