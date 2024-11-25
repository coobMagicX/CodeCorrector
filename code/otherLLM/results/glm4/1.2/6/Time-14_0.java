public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        
        // Handling the addition of months considering leap years
        int monthToAdd = valueToAdd;
        if (monthToAdd > 0) {
            while (monthToAdd > 0) {
                if (iChronology.getMonthOfYear(instant) == 2 && !iChronology.isLeapYear(iChronology.getYear(instant))) {
                    // If it's February in a non-leap year, we need to handle the transition from February 29
                    if (get(instant) > 2) {
                        instant = iChronology.addDays(iChronology.set(instant, 3), -1); // Move to March 1st
                        instant = iChronology.addMonths(instant, 1); // Add one month to adjust the date
                    } else {
                        instant = iChronology.addDays(instant, -1); // Adjust the day to February 28th
                        break; // Stop adding months as we have reached the end of February
                    }
                } else {
                    instant = iChronology.addMonths(instant, 1);
                }
                monthToAdd--;
            }
        } else { // Handling negative month addition
            while (monthToAdd < 0) {
                if (iChronology.getMonthOfYear(instant) == 3 && !iChronology.isLeapYear(iChronology.getYear(instant))) {
                    // If it's March in a non-leap year, we need to handle the transition from February 29
                    instant = iChronology.addDays(iChronology.set(instant, 2), -1); // Move to February 28th
                    instant = iChronology.addMonths(instant, -1); // Subtract one month to adjust the date
                } else {
                    instant = iChronology.addMonths(instant, monthToAdd);
                }
                monthToAdd++;
            }
        }

        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}