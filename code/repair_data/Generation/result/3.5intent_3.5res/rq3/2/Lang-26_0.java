public String format(Date date) {
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone(mTimeZone));
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}