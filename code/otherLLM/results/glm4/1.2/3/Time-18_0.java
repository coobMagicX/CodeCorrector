public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException {
    Chronology base;
    if ((base = getBase()) != null) {
        // Use the provided base chronology
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Assume date is Gregorian.
    long instant;
    try {
        instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
            hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            ChronoLocalDate julianDate = iJulianChronology.date(year, monthOfYear, dayOfMonth).at(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (julianToGregorianByYear(julianDate.toInstant().getMillis()) != instant) {
                // The Gregorian date doesn't match the Julian date conversion
                throw new IllegalArgumentException("Specified date does not exist in either calendar");
            }
        }
    } catch (DateTimeException e) {
        // If an exception is thrown, it means the specified date does not exist in the Gregorian chronology.
        try {
            instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (instant >= iCutoverMillis) {
                // If the date is after the cutover, it's not valid in Julian.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        } catch (DateTimeException e2) {
            // The specified date does not exist in the Julian chronology either.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }
    return instant;
}