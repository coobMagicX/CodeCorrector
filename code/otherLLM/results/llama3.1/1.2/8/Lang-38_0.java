public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced && calendar.getTimeZone().getID().equals("GMT-08:00")) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
    }
    return applyRules(calendar, buf);
}