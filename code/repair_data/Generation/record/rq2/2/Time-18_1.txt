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

    // First attempt with the Julian chronology.
    long instant = iJulianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    // Check if the calculated instant is before the cutover to Gregorian.
    if (instant < iCutoverMillis) {
        return instant;
    }
    
    // If the Julian date is not valid or it's after the cutover, calculate using Gregorian.
    instant = iGregorianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    if (instant < iCutoverMillis) {
        // Handle situation where the Gregorian date is before the cutover but invalid in Julian.
        throw new IllegalArgumentException("Specified date does not exist in the Julian calendar");
    }
    return instant;
}