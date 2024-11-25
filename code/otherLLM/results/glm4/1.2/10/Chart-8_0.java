public Week(Date time, TimeZone zone, Locale locale) {
    // Ensure the input parameters are not null
    if (time == null || zone == null || locale == null) {
        throw new IllegalArgumentException("Time, timezone or locale cannot be null");
    }

    // Use the provided timezone and locale for calculations
    this.time = time;
    this.zone = zone;
    this.locale = locale;

    // Create a Calendar instance with the given timezone and locale
    this.calendar = Calendar.getInstance(zone, locale);

    // Set the input date to the calendar
    this.calendar.setTime(time);

    // Adjust the week number calculation based on the locale's first day of the week
    int startDayOfWeek;
    if (locale.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, locale).equals(locale.getDisplayName(Calendar.SUNDAY, Calendar.LONG, locale))) {
        startDayOfWeek = Calendar.SUNDAY;
    } else {
        startDayOfWeek = Calendar.MONDAY;
    }

    // Set the calendar's first day of week to match the locale
    this.calendar.setFirstDayOfWeek(startDayOfWeek);

    // Compute the week number according to the locale settings
    int weekNumber = this.calendar.get(Calendar.WEEK_OF_YEAR);
    
    // Store the computed week number
    this.weekNumber = weekNumber;
}