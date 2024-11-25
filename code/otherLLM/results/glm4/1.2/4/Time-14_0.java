public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    boolean isAddingMonths = fieldIndex >= FieldUtils.MONTH && fieldIndex < FieldUtils.YEAR;
    int month = values[fieldIndex];

    // Handle the case where we are adding months to a leap year's February
    if (isAddingMonths && DateTimeUtils.isLeapYear(partial.getYear()) && partial.getMonth() == 2) {
        if (month + valueToAdd > 12) {
            // Add necessary years and adjust month accordingly
            int yearsToAdd = (month + valueToAdd - 1) / 12;
            values[fieldIndex] = ((month - 1) % 12) + 1;
            values[fieldIndex - 1] += yearsToAdd; // Add the years to the previous field
        } else if (valueToAdd > 0 && month == 2) {
            // If adding months and it's February, check for leap year overflow
            int newMonth = ((month + valueToAdd) % 12) + 1;
            values[fieldIndex] = newMonth;

            // Check if the resulting date is still in February
            if (newMonth == 2 && DateTimeUtils.isLeapYear(partial.getYear() - yearsToAdd)) {
                values[fieldIndex] = 2; // February, but not leap year's February
            }
        } else {
            values[fieldIndex] += valueToAdd;
        }
    } else {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(instant, iChronology).set(instant, values[i]);
        }

        if (isAddingMonths) {
            // If adding months, adjust the instant accordingly
            int yearsToAdd = (month + valueToAdd - 1) / 12;
            values[fieldIndex] = ((month - 1) % 12) + 1;
            values[fieldIndex - 1] += yearsToAdd; // Add the years to the previous field
        }

        instant = add(instant, valueToAdd);
        return iChronology.get(partial, instant);
    }
}