public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // e.g., 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }

    if (partial.isSupported(DateTimeFieldType.monthOfYear())) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);

        // After adding, retrieve the possibly new valid maximum for day of month
        int[] newValues = iChronology.get(partial, instant);
        if (partial.isSupported(DateTimeFieldType.dayOfMonth())) {
            int dayIndex = partial.indexOf(DateTimeFieldType.dayOfMonth());
            int maxDayOfMonth = iChronology.dayOfMonth().getMaximumValue(instant);
            if (newValues[dayIndex] > maxDayOfMonth) {
                newValues[dayIndex] = maxDayOfMonth;  // Adjust day to maximum allowed value
            }
        }

        return newValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}
