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

    // Determine the correct calendar system based on the date.
    long instant;
    if (isLeapYear(year)) {
        // If the year is a leap year, use the Julian chronology.
        instant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } else if (instant < iCutoverMillis) {
        // If the date is before the cutover gap, use the Julian chronology.
        instant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } else {
        // Otherwise, assume it's in the Gregorian calendar system.
        instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    if (instant < 0) {
        // If the resulting instant is negative, it's likely due to an invalid date.
        throw new IllegalArgumentException("Specified date does not exist");
    }
    return instant;
}

private boolean isLeapYear(int year) {
    // A year is a leap year if it is evenly divisible by four, except for end-of-century years
    // (years ending in 00). The exception to the rule is that years ending in 00 are only leap 
    // years if they are also evenly divisible by 400. This means that the year 2000 was a leap year,
    // although 1900 was not.
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
}