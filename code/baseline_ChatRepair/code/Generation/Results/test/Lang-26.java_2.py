public String format(Date date) {
    // Create a GregorianCalendar with the specific timezone
    Calendar calendar = new GregorianCalendar(mTimeZone);

    // Set the time of the calendar to the provided date
    calendar.setTime(date);

    // Set according to ISO 8601 where the first week of the year
    // is the week with at least 4 days and starts