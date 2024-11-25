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

    if (instant < iCutoverMillis) {
        // Convert from Gregorian to Julian if before the cutover.
        long julianInstant = julianToGregorianByYear(instant);
        // Check if the date in the Julian calendar is within the transition period.
        if (julianInstant >= iJulianCutoverMillis && julianInstant < iCutoverMillis) {
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }

    return instant;
}