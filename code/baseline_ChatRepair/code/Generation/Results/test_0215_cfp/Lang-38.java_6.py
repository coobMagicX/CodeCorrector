public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
        calendar.setTimeInMillis(calendar.getTimeInMillis()); // Recalculate the time fields with the new time zone
    }
    return applyRules(calendar, buf);
}
