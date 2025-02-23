public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
    throws IllegalArgumentException
{
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    
    try {
        long julianInstant = iJulianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        
        if (julianInstant >= iCutoverMillis) {
            return julianInstant;
        }
    } catch (IllegalFieldValueException e) {
        
    }

    
    long gregorianInstant = iGregorianChronology.getDateTimeMillis(
        year, monthOfYear, dayOfMonth,
        hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    
    if(gregorianInstant < iCutoverMillis) {
        throw new IllegalArgumentException("Specified date does not exist in Gregorian calendar, and is before cutover date.");
    }