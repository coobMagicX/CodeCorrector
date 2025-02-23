public String format(Date date) {
    // Create a GregorianCalendar with the specific time zone
    Calendar c = new GregorianCalendar(mTimeZone);

    // Set the date
    c.setTime(date);

    // Ensure that the calendar uses the ISO 8601 standard for week numbering
    c.setMinimalDaysInFirstWeek(4);
    c.setFirstDayOfWeek(Calendar.MONDAY);

    // Use