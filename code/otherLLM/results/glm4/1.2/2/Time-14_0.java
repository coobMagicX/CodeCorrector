public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = DateTimeUtils.truncateToMidnight(instant(), ChronoField.MONTH_OF_YEAR);
        for (int i = 0; i < partial.size(); i++) {
            Field<?> fieldType = partial.getFieldType(i);
            if (fieldType ==ChronoField.MONTH_OF_YEAR) {
                int newMonthValue = values[i] + valueToAdd;
                // Handle the case where the month goes beyond December
                while (newMonthValue > 12) {
                    newMonthValue -= 12;
                }
                // Handle the case where the month goes below January
                while (newMonthValue < 1) {
                    newMonthValue += 12;
                }
                values[i] = newMonthValue;
            } else {
                instant = fieldType.getField(iChronology).set(instant, values[i]);
            }
        }
        
        // Adjust for leap year transition from February 29th to January 29th
        if (DateTimeUtils.isLeapYear(year()) && month() == 2 && dayOfMonth() == 29) {
            if (valueToAdd < 0) {
                if (values[fieldIndex] + valueToAdd <= 0) {
                    // If the result is before February 29th, set to January 29th
                    values[ChronoField.MONTH_OF_YEAR.index()] = 1;
                    values[ChronoField.DAY_OF_MONTH.index()] = 29;
                }
            } else if (valueToAdd > 0) {
                // If the result is after February 29th and it's not a leap year, handle non-leap year transition
                long nonLeapYearInstant = iChronology.set(instant, ChronoField.YEAR, year() % 4);
                if (!DateTimeUtils.isLeapYear(year() + (year() % 4))) {
                    instant = nonLeapYearInstant;
                }
            }
        }
        
        instant = add(instant, valueToAdd);
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}