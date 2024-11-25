public String format(Date date) {
    Calendar c = new GregorianCalendar(TimeZone.getTimeZone(mTimeZone));
    c.setTime(date);
    StringBuffer buffer = new StringBuffer(mMaxLengthEstimate);
    applyRules(c, buffer);
    return buffer.toString();
}

private void applyRules(Calendar calendar, StringBuffer buffer) {
    SimpleDateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");
    SimpleDateFormat weekNumberFormat = new SimpleDateFormat("ww");

    String dayOfWeek = dayOfWeekFormat.format(calendar.getTime());
    String weekNumStr = weekNumberFormat.format(calendar.getTime());

    if (mLocale.equals("sv")) {
        buffer.append(dayOfWeek).append(", vecka ").append(weekNumStr);
    } else {
        // Add other locale formats as needed
    }
}