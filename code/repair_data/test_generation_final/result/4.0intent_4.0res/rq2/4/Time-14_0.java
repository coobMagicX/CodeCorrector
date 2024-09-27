public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    // Check if the field being added to is the month field and handle leap year cases
    if (fieldIndex == partial.getChronology().monthOfYear().getIndex() && DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        // Check if the initial date is February 29 of a leap year
        if (partial.getChronology().monthOfYear().get(instant) == 2 && partial.getChronology().dayOfMonth().get(instant) == 29) {
            // Add months and check if the resulting year is a leap year
            int totalMonths = partial.getChronology().monthOfYear().get(instant) + valueToAdd;
            int targetYear = partial.getChronology().year().get(instant) + (totalMonths / 12);
            if (!partial.getChronology().isLeapYear(targetYear) && totalMonths % 12 == 2) {
                // Adjust the day to the last valid day of February in a non-leap year
                values[partial.getChronology().dayOfMonth().getIndex()] = 28;
            }
        }
        instant = add(instant, valueToAdd);
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}