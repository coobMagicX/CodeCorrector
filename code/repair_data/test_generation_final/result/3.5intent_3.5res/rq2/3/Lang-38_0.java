public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        Calendar clonedCalendar = (Calendar) calendar.clone();
        clonedCalendar.setTimeZone(mTimeZone);
        return applyRules(clonedCalendar, buf);
    } else {
        calendar.setTimeZone(mTimeZone);
        return applyRules(calendar, buf);
    }
}