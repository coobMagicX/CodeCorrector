public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond)
        throws IllegalArgumentException {
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Assume date is Gregorian.
    long instant;
    instant = convertByYear(iGregorianChronology.getDateTimeMillis, iGregorianChronology, this);
    if (instant < iCutoverMillis) {
        // Maybe it's Julian.
        instant = convertByYear(iJulianChronology.getDateTimeMillis, iJulianChronology, this);
        if (instant >= iCutoverMillis) {
            // Okay, it's in the illegal cutover gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }
    return instant;
}