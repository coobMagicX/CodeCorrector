public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    return format(c, new StringBuffer(mMaxLengthEstimate)).toString();
}

public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar.getTimeInMillis(); /// LANG-538
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}