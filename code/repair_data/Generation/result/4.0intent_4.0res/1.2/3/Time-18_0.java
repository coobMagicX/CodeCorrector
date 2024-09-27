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
    // First, attempt to calculate using the Julian chronology.
    instant = iJulianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    // Check if the calculated instant is before the Gregorian cutover.
    if (instant >= iCutoverMillis) {
        // If it is not, then re-calculate using the Gregorian chronology.
        instant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        // If the Gregorian instant is still before the cutover, it's in the gap.
        if (instant < iCutoverMillis) {
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }
    return instant;
}