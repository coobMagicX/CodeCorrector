public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
    throws IllegalArgumentException
{
    Chronology base;
    if ((base = getBase()) != null) {
        if (isGregorian(year)) {
            return base.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        } else if (isJulian(year)) {
            return iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        } else {
            throw new IllegalArgumentException("Unsupported chronology");
        }
    }

    // Assume date is Gregorian.
    long instant = iGregorianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    
    if (instant < iCutoverMillis && isJulian(year)) {
        // Maybe it's Julian and before the cutover.
        instant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } else if (instant >= iCutoverMillis && isGregorian(year)) {
        // Okay, it's in the illegal cutover gap.
        throw new IllegalArgumentException("Specified date does not exist");
    }
    
    return instant;
}

private boolean isJulian(int year) {
    return year < 1582 || (year == 1582 && monthOfYear <= 9);
}

private boolean isGregorian(int year) {
    return year >= 1582;
}