public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone, new Locale("sv", "SE"));
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}