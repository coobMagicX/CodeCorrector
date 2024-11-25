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

    long instant;
    // Assume date is Gregorian.
    instant = iGregorianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    if (instant < iCutoverMillis) {
        // Maybe it's Julian.
        instant = julianToGregorianByYear(iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond));
        if (instant >= iCutoverMillis) {
            // Okay, it's in the illegal cutover gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    } else {
        instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    return instant;
}