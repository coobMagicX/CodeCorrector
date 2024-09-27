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
        instant = add(instant, valueToAdd);
        return iChronology.get(partial, instant);
    } else {
        // check if the original date is February 29 of a leap year
        DateTimeFieldType fieldType = partial.getFieldType(fieldIndex);
        if (fieldType.equals(DateTimeFieldType.monthOfYear()) && values[fieldIndex] == 2 && partial.isLeapYear()) {
            int year = partial.get(DateTimeFieldType.year());
            if (values[DateTimeFieldType.monthOfYear()] - valueToAdd < 0) {
                // subtracting months leads to a non-leap year
                values[fieldIndex] = 1; // set month to January
                if (!partial.getChronology().isLeapYear(year - 1)) {
                    // subtracting months from a leap day in a leap year
                    // leads to a non-leap year, adjust the resulting date
                    int day = partial.get(DateTimeFieldType.dayOfMonth());
                    int maxDay = partial.getChronology().dayOfMonth().getMaximumValue(year - 1, 2); // get the last valid day of February in non-leap years
                    if (day > maxDay) {
                        values[DateTimeFieldType.dayOfMonth()] = maxDay;
                    }
                }
            }
        }
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}