public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }

        // Adjusting for negative months by subtracting the difference in years and months
        long subtrahendInstant = instant;
        int minuendYear = iChronology.getYear(subtrahendInstant);
        int minuendMonth = iChronology.getMonthOfYear(subtrahendInstant, minuendYear);
        int subtrahendYear = iChronology.getYear(instant);
        int subtrahendMonth = iChronology.getMonthOfYear(instant, subtrahendYear);

        long difference = getDifferenceAsLong(subtrahendInstant, instant);

        // Adjust for the negative value to be added
        if (valueToAdd < 0) {
            difference += -valueToAdd;
        } else {
            difference -= valueToAdd;
        }

        int yearChange = (int) (difference / ((long) iMax));
        int monthChange = (int) (difference % ((long) iMax));

        // Update the instant by adjusting years and months
        subtrahendYear += yearChange;
        subtrahendMonth += monthChange;

        // Handle wrapping around the end of the year or beginning of the year
        if (subtrahendMonth < 0) {
            subtrahendMonth += iMax;
            subtrahendYear -= 1;
        } else if (subtrahendMonth >= iMax) {
            subtrahendMonth -= iMax;
            subtrahendYear += 1;
        }

        // Set the new year and month onto the instant
        long newInstant = iChronology.setYear(subtrahendInstant, subtrahendYear);
        newInstant = iChronology.setMonth(newInstant, subtrahendMonth);

        // Adjust for any overflow in months to days
        if (iChronology.isLeapYear(subtrahendYear) && subtrahendMonth == 2 && values[fieldIndex] > 29) {
            int daysInYear = iChronology.getDaysInYear(subtrahendYear);
            values[fieldIndex] -= daysInYear;
            subtrahendMonth += 1; // Go to the next month
        }

        instant = addWrapField(newInstant, subtrahendMonth - iChronology.getMonthOfYear(instant, subtrahendYear));

        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}