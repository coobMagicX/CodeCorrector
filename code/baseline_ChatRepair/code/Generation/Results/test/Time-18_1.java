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

    
    long instant;
    try {
        instant = iGregorianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        
        if (instant < iCutoverMillis) {
            
            long julianInstant = iJulianChronology.getDateTimeMillis(
                year, monthOfYear, dayOfMonth,
                hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

            
            if (julianInstant >= iCutoverMillis) {
                
                throw new IllegalArgumentException("Specified date does not exist");
            }

            
            return julianInstant;
        }

    
    } catch (IllegalFieldValueException e) {
        
        long julianInstant = iJulianChronology.getDateTimeMillis(
            year, month