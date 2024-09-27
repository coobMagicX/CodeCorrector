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
        // Check if the original date is February 29 of a leap year
        if (partial.get(DateTimeFieldType.year()).isLeap(values[partial.indexOf(DateTimeFieldType.year())]) &&
            values[partial.indexOf(DateTimeFieldType.monthOfYear())] == 2 &&
            values[partial.indexOf(DateTimeFieldType.dayOfMonth())] == 29) {
            // Adjust the target month and day if necessary after addition
            instant = add(instant, valueToAdd);
            int yearAfterAddition = iChronology.year().get(instant);
            if (!iChronology.year().isLeap(yearAfterAddition) && 
                iChronology.monthOfYear().get(instant) == 2 &&
                iChronology.dayOfMonth().get(instant) > 28) {
                instant = iChronology.dayOfMonth().set(instant, 28); // Set to Feb 28 if not a leap year
            }
        } else {
            instant = add(instant, valueToAdd);
        }
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}