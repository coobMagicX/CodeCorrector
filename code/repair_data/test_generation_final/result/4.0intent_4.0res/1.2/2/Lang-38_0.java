public StringBuffer format(Calendar calendar, StringBuffer buf) {
    Calendar tempCalendar = (Calendar) calendar.clone();
    if (mTimeZoneForced) {
        tempCalendar.setTimeZone(mTimeZone);
    }
    return applyRules(tempCalendar, buf);
}