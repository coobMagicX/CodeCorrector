public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
throws IllegalArgumentException {
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                       hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    long instant;
    // Assume date is Gregorian by default.
    instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                    hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    // Check if the date falls before the cutover date.
    if (instant < iCutoverMillis) {
        // It's Julian if it's before the cutover date.
        ChronoLocalDate jdate = iJulianChronology.date(year, monthOfYear, dayOfMonth);
        boolean isLeapYear = jdate.isLeapYear();
        
        // If the year is not a leap year in the Julian calendar, throw an exception.
        if (!isLeapYear) {
            throw new IllegalArgumentException("Specified date does not exist");
        }

        // Convert to Gregorian if it's valid and before the cutover date.
        instant = julianToGregorianByYear(instant);
    } else {
        // It's in the illegal cutover gap.
        throw new IllegalArgumentException("Specified date does not exist");
    }
    
    return instant;
}