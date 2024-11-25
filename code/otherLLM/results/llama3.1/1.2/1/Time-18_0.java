public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
    throws IllegalArgumentException
{
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Correctly handle Julian calendar leap years
    long instant;
    if (isJulianLeapYear(year)) {
        instant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } else {
        instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Check for dates in the illegal cutover gap
    if (instant >= iCutoverMillis && instant < iGregorianCutoverMillis) {
        throw new IllegalArgumentException("Specified date does not exist");
    }
    return instant;
}

// Helper method to check if a year is a leap year in the Julian calendar
private boolean isJulianLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
}