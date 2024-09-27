public String format(Date date) {
    // Set the locale for the GregorianCalendar to ensure locale-specific formatting
    Calendar c = new GregorianCalendar(mTimeZone, mLocale);
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}