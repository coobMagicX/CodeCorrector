public Week(Date time, TimeZone zone) {
    // defer argument checking...
    this(time, getWeekNumber(time, zone), Locale.getDefault());
}

private int getWeekNumber(Date date, TimeZone tz) {
    Calendar cal = new GregorianCalendar(tz);
    cal.setTime(date);
    return cal.get(Calendar.WEEK_OF_YEAR);
}