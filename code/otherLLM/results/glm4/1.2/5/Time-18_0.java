public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
    throws IllegalArgumentException {
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Assume date is Gregorian.
    long instant;
    try {
        instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } catch (IllegalArgumentException e) {
        if (isJulian(year, monthOfYear, dayOfMonth)) {
            // If the date is in the Julian calendar and not valid there, throw an exception.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        } else if (instant < iCutoverMillis) {
            // If the date is before the cutover and not valid there, throw an exception.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }

    return instant;
}

private boolean isJulian(int year, int monthOfYear, int dayOfMonth) {
    if (monthOfYear == 2 && dayOfMonth == 29) {
        // Check if the year is a leap year in the Julian calendar.
        return (year % 4 == 0);
    }
    // The rest of the months and days are valid in the Julian calendar, so we just return true here.
    return true;
}