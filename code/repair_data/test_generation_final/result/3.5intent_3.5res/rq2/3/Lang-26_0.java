public String format(Date date) {
    Calendar c = Calendar.getInstance(mTimeZone, new Locale("sv", "SE"));
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}