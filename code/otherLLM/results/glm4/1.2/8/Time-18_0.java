public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException {
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                      hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Assume date is Gregorian.
    long instant;
    instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                     hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    if (year >= 1582 && year <= 1700) {
        // Check for leap year in the Gregorian calendar.
        boolean isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        if (monthOfYear == 2 && dayOfMonth == 29 && !isLeapYear) {
            // If it's February 29 and not a leap year, throw an IllegalArgumentException.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }

    if (instant < iCutoverMillis) {
        // Maybe it's Julian. Check for leap year in the Julian calendar.
        boolean isLeapYear = (year % 4 == 0);
        if (monthOfYear == 2 && dayOfMonth == 29 && !isLeapYear) {
            // If it's February 29 and not a leap year, calculate as in Julian calendar
            // that every fourth year is a leap year.
            if ((year - 1) % 4 == 0) {
                instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                               hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            } else {
                // If it's February 29 and not a leap year in Julian, throw an IllegalArgumentException.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        } else {
            instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                           hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }

        if (instant >= iCutoverMillis) {
            // Okay, it's in the illegal cutover gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }

    return instant;
}