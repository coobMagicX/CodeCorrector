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

    // Select the correct chronology based on the input year and date.
    if ((year < 1582 || (year == 1582 && monthOfYear == 10 && dayOfMonth >= 5)) &&
        iJulianChronology.isLeapYear(year)) {
        return iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } else {
        // Assume date is Gregorian.
        long instant;
        instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis
                (year, monthOfYear, dayOfMonth,
                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (instant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
        return instant;
    }
}