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
        // First attempt to calculate using the Gregorian chronology.
        long instant = iGregorianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        // If the result is before the cutover instant, check using the Julian chronology.
        if (instant < iCutoverMillis) {
            long julianInstant = iJulianChronology.getDateTimeMillis(
                year, monthOfYear, dayOfMonth,
                hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (julianInstant >= iCutoverMillis) {
                // If the Julian instant is in the cutover, it's an invalid date.
                throw new IllegalArgumentException("Specified date does not exist");
            }
            return julianInstant;
        }
        return instant;
    } catch (IllegalFieldValueException e) {
        // If the Gregorian date is invalid, check the Julian calendar for leap year situations.
        long julianInstant = iJulianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        if (julianInstant >= iCutoverMillis) {
            //Same check for Julian dates falling in the cutover period.
            throw new IllegalArgumentException("Specified date does not exist");
        }
        return julianInstant;
    }
}
