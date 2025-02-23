public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // e.g., 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }

    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);

        int[] newValues = iChronology.get(partial, instant);
        // Check for day of month which might overflow due to month change
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            if (partial.getFieldType(i) == DateTimeFieldType.dayOfMonth()) {
                int maxDayOfMonth = iChronology.dayOfMonth().getMaximumValue(instant);
                if (newValues[i] > maxDayOfMonth) {
                    newValues[i] = maxDayOfMonth; // adjust day to maximum allowed value
                }
                break; // stop once dayOfMonth has been checked
            }
        }
        return newValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}
