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
    try {
        instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                         hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

        if (instant < iCutoverMillis) {
            // Convert to Julian if the date is before the cutover.
            Chronology julian = Chronology.of(iJulianChronology);
            long julianInstant = julianToGregorianByYear(instant);
            
            if (julianInstant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
    } catch (DateTimeException e) {
        try {
            // Maybe it's Julian. Attempt to create a Julian Chronology instance
            // and convert if necessary.
            Chronology julian = Chronology.of(iJulianChronology);
            instant = julian.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                 hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);

            if (instant < iCutoverMillis) {
                // Convert from Julian to Gregorian if the date is before the cutover.
                long gregorianInstant = julianToGregorianByYear(instant);
                
                if (gregorianInstant >= iCutoverMillis) {
                    // Okay, it's in the illegal cutover gap.
                    throw new IllegalArgumentException("Specified date does not exist");
                }
            } else {
                // Adjust instant to Gregorian chronology since after the cutover
                instant = gregorianToJulianByYear(instant);
            }
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Specified date does not exist", ex);
        }
    }

    return instant;
}