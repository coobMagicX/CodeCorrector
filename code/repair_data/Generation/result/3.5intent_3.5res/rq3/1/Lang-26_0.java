public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    return applyRules(c, new StringBuilder(mMaxLengthEstimate)).toString();
}