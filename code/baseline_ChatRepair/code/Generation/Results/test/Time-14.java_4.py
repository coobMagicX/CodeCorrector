public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    if (valueToAdd == 0) {
        return values;
    }
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);

        // Attempt to get the values for the new instant
        try {
            return iChronology.get(partial, instant);
        } catch (IllegalFieldValueException ex) {
            // Check if modification of the day of month is necessary
            if (fieldIndex == indexOfDateTimeField(partial, DateTimeFieldType.monthOfYear()) &&
                isSupportedDateTimeField(partial, DateTimeFieldType.dayOfMonth())) {
                // Adjust day of month if out of range
                int dayIndex = indexOfDateTimeField(partial, DateTimeFieldType.dayOfMonth());
                if (dayIndex >= 0) {
                    int lastDayOfMonth = iChronology.dayOfMonth().getMaximumValue(instant);
                    values[dayIndex] = lastDayOfMonth;
                    instant = partial.getFieldType(dayIndex).getField(iChronology).set(instant, lastDayOfMonth);
                }
                return iChronology.get(partial, instant);
            } else {
                throw ex; // rethrow original exception if the conditions are not met
            }
        }
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

private int indexOfDateTimeField(ReadablePartial partial, DateTimeFieldType fieldType) {
    for (int i = 0, isize = partial.size(); i < isize; i++) {
        if (partial.getFieldType(i) == fieldType) {
            return i;
        }
    }
    return -1; // Field not found
}

private boolean isSupportedDateTimeField(ReadablePartial partial, DateTimeFieldType fieldType) {
    for (int i = 0, isize = partial.size(); i < isize; i++) {
        if (partial.getFieldType(i) == fieldType) {
            return true;
        }
    }
    return false;
}
