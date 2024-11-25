public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
throws IllegalArgumentException
{
    Chronology base = getBase();
    if (base != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                       hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Assume date is Gregorian.
    long instant;
    instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                    hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

    // Check if the date is before the cutover and possibly in the Julian calendar
    if (instant < iCutoverMillis) {
        Chronology julian = getJulianChronology();
        if (julian != null && julian.isSupported(year, monthOfYear, dayOfMonth)) {
            instant = julian.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                               hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        }
    }

    // If the calculated instant is still before the cutover and not properly handled by the Julian calendar
    if (instant < iCutoverMillis) {
        throw new IllegalArgumentException("Specified date does not exist");
    }

    return instant;
}

// Method to get Julian Chronology, which might need to be defined in your class or context.
private Chronology getJulianChronology() {
    // Placeholder for actual implementation
    return null; // This should return an instance of Julian chronology if available
}