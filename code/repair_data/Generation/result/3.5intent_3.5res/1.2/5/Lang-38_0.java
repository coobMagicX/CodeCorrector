public StringBuffer format(Calendar calendar, StringBuffer buf) {
    Calendar convertedCalendar = (Calendar) calendar.clone();

    if (mTimeZoneForced) {
        convertedCalendar.setTimeZone(mTimeZone);
    } else {
        convertedCalendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    return applyRules(convertedCalendar, buf);
}