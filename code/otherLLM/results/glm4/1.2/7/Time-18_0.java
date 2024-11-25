public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
throws IllegalArgumentException
{
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                      hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Assume date is Gregorian.
    long instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    if (isJulianDate(year, monthOfYear, dayOfMonth)) {
        // It's a Julian date that should be recognized by the Julian calendar.
        instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                     hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    if (instant >= iCutoverMillis) {
        // It's in the illegal cutover gap.
        throw new IllegalArgumentException("Specified date does not exist");
    }

    return instant;
}

private boolean isJulianDate(int year, int monthOfYear, int dayOfMonth) {
    // Check for historical dates that should be recognized by the Julian calendar
    // before the Gregorian cutover. For example, 1500-10-04 was the last valid date
    // in the Julian calendar.
    if (year < 1582 || (year == 1582 && monthOfYear <= 10)) {
        return true;
    }
    return false;
}