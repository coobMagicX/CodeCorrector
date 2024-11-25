public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    
    long instant = partial.toInstant().getMillis();
    boolean isLeap = isLeap(instant);
    int year = iChronology.getYear(instant);
    int monthOfYear = iChronology.getMonthOfYear(instant);
    int dayOfMonth = iChronology.getDayOfMonth(instant);

    // Adjust the valueToAdd if it's negative
    if (valueToAdd < 0) {
        instant = subtract(instant, -valueToAdd);
    } else {
        instant = add(instant, valueToAdd);
    }

    // Update year, monthOfYear and dayOfMonth after adding/subtracting months
    year = iChronology.getYear(instant);
    monthOfYear = iChronology.getMonthOfYear(instant);

    // Adjust day of the month if necessary
    while (dayOfMonth > iChronology.getDayOfMonth(instant)) {
        // If we have more days in the original date than the new one,
        // subtract a day until it fits.
        dayOfMonth -= iChronology.getDayOfMonth(instant);
        monthOfYear--;
        if (monthOfYear < 1) {
            monthOfYear = iChronology.getMonthsInYear(year) - (iChronology.getMonthOfYear(instant) - 1);
            year--;
        }
    }

    // If the new month has fewer days than the original one, we need to roll back
    if (dayOfMonth > values[fieldIndex]) {
        while (dayOfMonth > iChronology.getDayOfMonth(instant)) {
            dayOfMonth -= iChronology.getDayOfMonth(instant);
            monthOfYear--;
            if (monthOfYear < 1) {
                monthOfYear = iChronology.getMonthsInYear(year) - (iChronology.getMonthOfYear(instant) - 1);
                year--;
            }
        }
    }

    // Set the values back into the array
    values[fieldIndex] = dayOfMonth;
    if (fieldIndex <= 4) { // Only set month and year for first 5 fields, assuming month-day format
        values[fieldIndex + 1] = monthOfYear;
        values[0] = year; // Set the year to the first index of the array
    }

    return values;
}

// Method to subtract months from an instant
private long subtract(long instant, int valueToAdd) {
    for (int i = 0; i < Math.abs(valueToAdd); i++) {
        if (isLeap(instant)) {
            // If it's a leap year and February is being reduced
            if (iChronology.getMonthOfYear(instant) == 2 && iChronology.getDayOfMonth(instant) > 29) {
                instant = iChronology.addMonth(instant, -1);
                instant = iChronology.setDayOfMonth(instant, 28);
            } else {
                instant = iChronology.addMonth(instant, -1);
            }
        } else {
            // If it's not a leap year and February is being reduced
            if (iChronology.getMonthOfYear(instant) == 2 && iChronology.getDayOfMonth(instant) > 28) {
                instant = iChronology.addMonth(instant, -1);
                instant = iChronology.setDayOfMonth(instant, 27);
            } else {
                instant = iChronology.addMonth(instant, -1);
            }
        }
    }
    return instant;
}

// Method to add months to an instant
private long add(long instant, int valueToAdd) {
    for (int i = 0; i < Math.abs(valueToAdd); i++) {
        if (isLeap(instant)) {
            // If it's a leap year and February is being increased
            if (iChronology.getMonthOfYear(instant) == 2 && iChronology.getDayOfMonth(instant) > 29) {
                instant = iChronology.addMonth(instant, 1);
                instant = iChronology.setDayOfMonth(instant, 29);
            } else {
                instant = iChronology.addMonth(instant, 1);
            }
        } else {
            // If it's not a leap year and February is being increased
            if (iChronology.getMonthOfYear(instant) == 2 && iChronology.getDayOfMonth(instant) > 28) {
                instant = iChronology.addMonth(instant, 1);
                instant = iChronology.setDayOfMonth(instant, 27); // Set to the last day of February
            } else {
                instant = iChronology.addMonth(instant, 1);
            }
        }
    }
    return instant;
}