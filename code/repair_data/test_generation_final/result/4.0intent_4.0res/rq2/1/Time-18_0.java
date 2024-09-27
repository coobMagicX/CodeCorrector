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

    // Assume date is initially Gregorian.
    long instant = iGregorianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    if (instant < iCutoverMillis) {
        // Attempt to use Julian Chronology since the instant is before the Gregorian cutover.
        long julianInstant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        
        if (julianInstant >= iCutoverMillis) {
            // If Julian instant crosses into the Gregorian cutover, convert the instant.
            instant = convertByYear(julianInstant, iJulianChronology, iGregorianChronology);
        } else {
            // If Julian instant is valid and doesn't cross into Gregorian cutover, use it.
            instant = julianInstant;
        }
    }
    
    // Ensure leap year rules are correctly applied by validating the day of month.
    if (dayOfMonth != iGregorianChronology.dayOfMonth().get(instant) &&
        dayOfMonth != iJulianChronology.dayOfMonth().get(instant)) {
        throw new IllegalArgumentException("Invalid day of month for input year and month");
    }
    
    return instant;
}