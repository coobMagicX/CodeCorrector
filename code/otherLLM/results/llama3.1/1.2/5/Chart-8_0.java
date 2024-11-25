public Week(Date time, TimeZone zone) {
    // defer argument checking...
    this(getWeekNumberFromDateTime(time, zone), getFirstDayOfWeek(), RegularTimePeriod.DEFAULT_TIME_ZONE, Locale.getDefault());
}

private int getWeekNumberFromDateTime(Date date, TimeZone timeZone) {
    Calendar calendar = Calendar.getInstance(timeZone);
    calendar.setTime(date);
    int year = calendar.get(Calendar.YEAR);
    if (isLeapYear(year)) {
        // Adjust for leap year
        return 1 + (getFirstDayOfWeek() - 1) / 7;
    } else {
        return getFirstDayOfWeek();
    }
}

private boolean isLeapYear(int year) {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
}