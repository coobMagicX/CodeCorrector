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

    // Assume date is Gregorian.
    long instant;
    iGregorianChronology.setYear(year);
    instant = iGregorianChronology.getDateTimeMillis
        (dayOfMonth, monthOfYear, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    if (instant < iCutoverMillis) {
        // Maybe it's Julian.
        iJulianChronology.setYear(year);
        long julianInstant = iJulianChronology.getDateTimeMillis
            (dayOfMonth, monthOfYear, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        if (julianInstant >= iCutoverMillis) {
            // Okay, it's in the illegal cutover gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }

        instant = julianInstant;
    }
    return instant;
}