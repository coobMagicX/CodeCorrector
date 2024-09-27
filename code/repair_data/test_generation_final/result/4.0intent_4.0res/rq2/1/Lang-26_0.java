public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    if (mTimeZoneForced) {
        c.getTimeInMillis(); // necessary to force Calendar internal recomputation
        c = (Calendar) c.clone();
        c.setTimeZone(mTimeZone);
    }
    StringBuffer buffer = new StringBuffer(mMaxLengthEstimate);
    return applyRules(c, buffer).toString();
}