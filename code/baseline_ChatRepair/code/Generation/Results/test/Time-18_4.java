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
            return checkJulianAndThrowIfInvalid(
                year, monthOfYear, dayOfMonth,
                hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
        return instant;
    } catch (IllegalFieldValueException e) {
        
        return checkJulianAndThrowIfInvalid(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }
}

private long checkJulianAndThrowIfInvalid(int year, int monthOfYear, int dayOfMonth,
                                          int hourOfDay, int minuteOfHour,
                                          int secondOfMinute, int millisOfSecond) {
    long julianInstant = iJulianChronology.getDateTimeMillis(
        year, monthOfYear, day