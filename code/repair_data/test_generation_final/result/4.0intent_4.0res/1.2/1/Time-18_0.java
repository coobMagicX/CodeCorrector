public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
    throws IllegalArgumentException
{
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis
            (year, monthOfYear, dayOfMonth,
             hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // First try with the Julian calendar.
    long instant = iJulianChronology.getDateTimeMillis
        (year, monthOfYear, dayOfMonth,
         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    // Check if the date falls into the cutover gap when switching to Gregorian.
    if (instant >= iCutoverMillis) {
        // If it's not in the cutover gap, convert to Gregorian to validate the date.
        long convertedInstant = convertByYear(instant, iJulianChronology, iGregorianChronology);
        if (convertedInstant >= iCutoverMillis) {
            // If the converted instant still falls after the cutover, use it.
            instant = convertedInstant;
        } else {
            // If conversion brings the date before the cutover, it's in the gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }
    return instant;
}