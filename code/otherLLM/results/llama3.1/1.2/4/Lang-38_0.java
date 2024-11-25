public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (getTimeZoneOverridesCalendar()) {
        Calendar forcedCalendar = (Calendar) calendar.clone();
        forcedCalendar.setTimeZone(mTimeZone);
        return applyRules(forcedCalendar, buf);
    }
    return applyRules(calendar, buf);
}