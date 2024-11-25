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

    if (isJulianYear(year)) {
        // If it's a Julian year, check for February 29th.
        if (monthOfYear == 2 && dayOfMonth == 29) {
            instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                          hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
    }

    if (instant < iCutoverMillis) {
        // Maybe it's Julian.
        if (!isLeapYear(instant)) {
            instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                          hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
    }

    if (instant >= iCutoverMillis) {
        // Okay, it's in the illegal cutover gap.
        throw new IllegalArgumentException("Specified date does not exist");
    }

    return instant;
}

// Additional methods that could be used:

public boolean isJulianYear(int year) {
    // This method should determine if a given year is a Julian year.
    // Example implementation:
    return year <= 1582;
}

public boolean isLeapYear(long millis) {
    // This method should determine if the date represented by 'millis' is a leap year in the Julian calendar.
    ZonedDateTime zdt = ZonedDateTime.ofInstant(millis, ZoneId.systemDefault());
    YearMonth ym = zdt.getMonth().atEndOfMonth();
    return ym.isLeapYear();
}