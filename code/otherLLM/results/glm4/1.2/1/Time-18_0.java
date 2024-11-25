public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth,
                              int hourOfDay, int minuteOfHour,
                              int secondOfMinute, int millisOfSecond)
    throws IllegalArgumentException {
    Chronology base = getBase();
    if (base != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                       hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    // Determine the chronology based on the date and apply leap year rules accordingly.
    boolean isLeapYear;
    long instant;

    if (year < 1582) { // Before the Gregorian reform in 1582, only Julian calendar was used.
        // Check for leap year in Julian calendar: divisible by 4 but not by 100 unless also divisible by 400
        isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    } else if (year > 1582) { // After the Gregorian reform, use the Gregorian calendar rules.
        // Check for leap year in Gregorian calendar: divisible by 4 but not by 100 unless divisible by 400
        isLeapYear = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
    } else { // Year 1582, check which chronology it falls under.
        if (monthOfYear < 10) { // Before October 1582, use Julian calendar
            isLeapYear = (dayOfMonth % 4 == 0 && (dayOfMonth % 100 != 0 || dayOfMonth % 400 == 0));
        } else {
            isLeapYear = false; // After October 1582, use Gregorian calendar, no leap years in this month
        }
    }

    // Adjust the number of days in February based on whether it's a leap year
    int daysInMonth = (isLeapYear && monthOfYear == 2) ? 29 : iGregorianChronology.getDaysInMonth(year, monthOfYear);

    // Now that we know the chronology and if it's a leap year, get the instant
    if (year < 1582 || (year == 1582 && monthOfYear < 10)) {
        // Before October 1582 or in the illegal cutover gap
        instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                       hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    } else if (year == 1582 && monthOfYear >= 10) {
        // In the illegal cutover gap
        throw new IllegalArgumentException("Specified date does not exist");
    } else {
        instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth,
                                                       hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    return instant;
}