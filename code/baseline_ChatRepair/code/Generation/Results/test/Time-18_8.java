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

    long julianInstant = iJulianChronology.getDateTimeMillis(
        year, monthOfYear, dayOfMonth,
        hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    if (julianInstant < iCutoverMillis) {
        
        return julianInstant;
    } else {
        
        long gregorianInstant = iGregorianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        if (gregorianInstant < iCutoverMillis) {
            throw new IllegalArgumentException("Date is before cutover but invalid in Gregorian calendar");
        }
        return gregorianInstant;
    }
}
