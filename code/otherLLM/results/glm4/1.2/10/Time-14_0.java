public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }

    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            // Get the field type and set it at the current index
            int fieldType = partial.getFieldType(i);
            if (fieldType == ChronoField.MONTH_OF_YEAR || fieldType == ChronoField.YEAR) {
                instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
            }
        }

        // If year or month is involved in the addition, adjust for leap years and chronology
        if (fieldIndex == ChronoField.MONTH_OF_YEAR || fieldIndex == ChronoField.YEAR) {
            instant = iChronology.add(years(iChronology.getYear(instant)), months(valueToAdd), instant);
            valueToAdd = 0; // Reset valueToAdd to avoid further additions
        }

        // If the addition is not zero, add it to the instant
        if (valueToAdd != 0) {
            instant += DateTimeUtils.convertValueToAdd(iChronology, partial.getFieldType(fieldIndex), valueToAdd);
        }

        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

private long years(int year) {
    return (long) year;
}

private long months(int month) {
    // Ensure month is in the range 0-11
    return Math.max(0, Math.min(11, month));
}