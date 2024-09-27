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
    long instant = iGregorianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    
    if (instant < iCutoverMillis) {
        // Maybe it's Julian.
        long julianInstant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        
        if (julianInstant >= iCutoverMillis) {
            // Okay, it's in the illegal cutover gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }

        // Use Julian date since it is valid and before the Gregorian cutover.
        instant = julianInstant;
    } else if (iGregorianChronology.isLeapYear(year) && monthOfYear == 2 && dayOfMonth == 29) {
        // Special case for leap year rule in Gregorian calendar during cutover year
        long testInstant = iGregorianChronology.getDateTimeMillis(year, 2, 28, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        if (testInstant >= iCutoverMillis) {
            // It's a leap year, but the leap day (Feb 29) is after the cutover, use Julian calendar
            instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
    }
    
    return instant;
}