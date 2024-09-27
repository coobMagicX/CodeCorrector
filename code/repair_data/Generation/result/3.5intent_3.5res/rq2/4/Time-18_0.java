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
    if (year >= 1582) {
        instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                        hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } else if (year == 1582 && monthOfYear == 10 && dayOfMonth >= 5 && dayOfMonth <= 14) {
        // Handle the special case of missing dates in the transition from Julian to Gregorian calendar
        throw new IllegalArgumentException("Specified date does not exist");
    } else {
        // Maybe it's Julian.
        instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                      hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        if (instant >= iCutoverMillis) {
            // Okay, it's in the illegal cutover gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }
    return instant;
}