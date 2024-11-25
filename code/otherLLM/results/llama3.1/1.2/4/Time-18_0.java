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

    // Check for cutover gap first.
    long instant = iJulianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    if (instant < iCutoverMillis) {
        // Okay, it's in the illegal cutover gap.
        throw new IllegalArgumentException("Specified date does not exist");
    }
    instant = iGregorianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    return instant;
}