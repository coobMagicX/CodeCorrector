public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.setMinimalDaysInFirstWeek(4);
    c.setTime(date);
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}
