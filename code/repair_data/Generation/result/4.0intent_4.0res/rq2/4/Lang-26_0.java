public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone, mLocale); // Ensure the locale is also set in the Calendar instance
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}