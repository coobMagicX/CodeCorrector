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

    
    long gregorianInstant = tryGetMillis(
        iGregorianChronology, year, monthOfYear, dayOfMonth,
        hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    
    if (gregorianInstant < iCutoverMillis) {
        long julianInstant = tryGetMillis(
            iJulianChronology, year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        
        if (julianInstant < iCutoverMillis) {
            return julianInstant;
        }
    }
    
    return gregorianInstant;  
}

private long tryGetMillis(Chronology chronology, int year, int monthOfYear, int dayOfMonth,
                          int hourOfDay, int minuteOfHour, int secondOfMinute, int millis