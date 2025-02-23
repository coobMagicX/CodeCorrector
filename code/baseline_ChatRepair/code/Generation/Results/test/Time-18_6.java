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

    long gregorianInstant = 0;
    boolean isGregorianValid = true;
    
    try {
        
        gregorianInstant = iGregorianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } catch (IllegalFieldValueException e) {
        isGregorianValid = false;
    }

    
    if (!isGregorianValid || gregorianInstant < iCutoverMillis) {
        long julianInstant = iJulianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        
        if (julianInstant >= iCutoverMillis) {
            
            throw new IllegalArgumentException("Specified date does not exist");
        }
        
        
        if (!isGregorianValid) {
            return julianInstant;
        }
    }
    
    
    return gregorianInstant;
}
