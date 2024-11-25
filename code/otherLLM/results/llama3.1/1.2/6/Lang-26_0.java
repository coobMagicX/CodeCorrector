public String format(Date date) {
    Calendar c = new GregorianCalendar(TimeZone.getTimeZone(mTimeZone));
    if (getTimeZoneOverridesCalendar()) {
        c.setTimeZone(TimeZone.getTimeZone(mTimeZone));
    }
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}