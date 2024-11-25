public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    
    StringBuffer buf = new StringBuffer(mMaxLengthEstimate);
    return this.format(c, buf).toString();
}

// Method to be used as is:
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar.getTimeInMillis(); // LANG-538
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}