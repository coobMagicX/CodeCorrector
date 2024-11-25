public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone, Locale.SWEDISH);
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate), Locale.SWEDISH).toString();
}