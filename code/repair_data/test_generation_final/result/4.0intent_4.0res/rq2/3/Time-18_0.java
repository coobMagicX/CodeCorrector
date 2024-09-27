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

    // Check both Julian and Gregorian chronologies to handle different validities
    long julianInstant = iJulianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    long gregorianInstant = iGregorianChronology.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    
    // Determine which instant to use based on cutover and validity in each chronology
    if (julianInstant < iCutoverMillis && gregorianInstant >= iCutoverMillis) {
        return julianInstant; // Julian valid, Gregorian in cutover gap
    } else if (gregorianInstant < iCutoverMillis) {
        return gregorianInstant; // Both Julian and Gregorian valid, but before cutover
    } else if (julianInstant >= iCutoverMillis && gregorianInstant < iCutoverMillis) {
        throw new IllegalArgumentException("Specified date does not exist in Julian calendar but exists in Gregorian calendar");
    } else {
        // Both are invalid or in the cutover gap
        throw new IllegalArgumentException("Specified date does not exist in either Julian or Gregorian calendar");
    }
}