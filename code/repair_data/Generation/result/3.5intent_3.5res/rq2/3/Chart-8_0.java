public Week(Date time, TimeZone zone) {
    this(time, zone, Locale.getDefault());
}

public Week(Date time, TimeZone zone, Locale locale) {
    super(time, zone);
    Calendar calendar = Calendar.getInstance(zone, locale);
    calendar.setTime(time);

    // Adjust week number calculation based on locale
    int firstDayOfWeek = calendar.getFirstDayOfWeek();
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    if (firstDayOfWeek == Calendar.MONDAY && dayOfWeek == Calendar.SUNDAY) {
        // If locale considers Monday as first day and current day is Sunday, adjust week number
        calendar.add(Calendar.DAY_OF_YEAR, -1);
    } else if (firstDayOfWeek == Calendar.SUNDAY && dayOfWeek == Calendar.MONDAY) {
        // If locale considers Sunday as first day and current day is Monday, adjust week number
        calendar.add(Calendar.DAY_OF_YEAR, 1);
    }

    // Calculate week number
    int weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
    this.week = weekNumber;
}