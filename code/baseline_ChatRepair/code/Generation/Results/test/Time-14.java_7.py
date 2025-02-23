public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    if (valueToAdd == 0) {
        return values;
    }
    if (DateTimeUtils.isContiguous(partial) && partial.getFieldType(fieldIndex).getName().equals(DateTimeFieldType.monthOfYear().getName())) {
        long instant = 0L;
        // Build the instant from the fields
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }

        // Compute the new instant with added months
        instant = iChronology.months().add(instant, valueToAdd);

        // Convert the new instant back to field values
        int[] newValues = iChronology.get(partial, instant);

        // Find the index of the day of the month field, if present
        int dayIndex = -1;
        for (int i = 0; i < partial.size(); i++) {
            if (partial.getFieldType(i) == DateTimeFieldType.dayOfMonth()) {
                dayIndex = i;
                break;
            }
        }

        // Adjust day of month if necessary
        if (dayIndex != -1) {
            int oldDay = values[dayIndex];
            int newDay = newValues[dayIndex];
            int maxDayInNewMonth = iChronology.dayOfMonth().getMaximumValue(instant);

            if (newDay > maxDayInNewMonth) {
                newValues[dayIndex] = maxDayInNewMonth;
            }
        }

        return newValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}
