public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null || buf == null) {
        throw new IllegalArgumentException("Arguments cannot be null");
    }

    Calendar calendarCopy = (Calendar) calendar.clone();

    if (mTimeZoneForced) {
        calendarCopy.setTimeZone(mTimeZone);
    }

    return applyRules(calendarCopy, buf);
}
