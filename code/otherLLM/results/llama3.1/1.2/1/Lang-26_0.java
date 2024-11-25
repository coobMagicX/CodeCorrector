public String format(Date date) {
    Calendar c = new GregorianCalendar(getLocale());
    c.setTime(date);
    c.setFirstDayOfWeek(c.getFirstDayOfWeek()); // Set first day of week to match locale
    c.setCalendarWeeksRule(Calendar.MONDAY); // Set week number rule to match locale
    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}