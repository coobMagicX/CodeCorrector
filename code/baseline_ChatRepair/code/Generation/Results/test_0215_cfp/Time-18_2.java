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
        long instant = iGregorianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        if (instant < iCutoverMillis) {
            
            long julianInstant = iJulianChronology.getDateTimeMillis(
                year, monthOfYear, dayOfMonth,
                hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            
            if (julianInstant < iCutoverMillis) {
                return julianInstant;
            }
        }
        return instant;
    } catch (IllegalFieldValueException ex) {
        
        long julianInstant = iJulianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        if (julianInstant < iCutoverMillis) {
            return julianInstant