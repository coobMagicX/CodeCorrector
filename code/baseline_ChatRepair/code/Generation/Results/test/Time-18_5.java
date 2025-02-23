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

    long gregorianInstant = -1;
    boolean gregorianValid = true;
    long julianInstant = -1;
    boolean julianValid = true;

    try {
        gregorianInstant = iGregorianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } catch (IllegalFieldValueException e) {
        gregorianValid = false;
    }

    try {
        julianInstant = iJulianChronology.getDateTimeMillis(
            year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } catch (IllegalFieldValueException e) {
        julianValid = false;
    }

    if (!gregorianValid && !julianValid) {
        throw new IllegalArgumentException("Specified date does not exist in either Gregorian or Julian calendars");
    }

    if (gregorianValid && gregorianInstant < iCutoverMillis) {
        