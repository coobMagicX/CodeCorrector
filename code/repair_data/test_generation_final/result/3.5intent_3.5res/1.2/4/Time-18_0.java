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

    long instant = iGregorianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    if (instant < iCutoverMillis) {
        // Check if the Gregorian date is before the Julian to Gregorian cutover,
        // then test if the date is valid in the Julian calendar.
        long julianInstant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        if (julianInstant < iCutoverMillis) {
            // If the Julian instant is also before the cutover, use the Julian date
            instant = julianInstant;
        } else {
            // The date falls into the cutover gap where it's not valid in either calendar
            throw new IllegalArgumentException("Specified date does not exist in either Julian or Gregorian calendar");
        }
    }

    return instant;
}

