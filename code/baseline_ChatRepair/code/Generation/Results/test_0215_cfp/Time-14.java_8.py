public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    if (DateTimeUtils.isContiguous(partial) && partial.getFieldType(fieldIndex).getName().equals(DateTimeFieldType.monthOfYear().getName())) {
        long instant = 0L;
        // Create an initial instant from the input partial and values
        for (int i = 0; i < partial.size(); i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }

        int dayOfMonthIndex = -1;
        // Find the position of the dayOfMonth field if it's supported
        for (int i = 0; i < partial.size(); i++) {
            if (partial.getFieldType(i).equals(DateTimeFieldType.dayOfMonth())) {
                dayOfMonthIndex = i;
                break;
            }
        }

        if (dayOfMonthIndex != -1) {
            int dayOfMonth = values[dayOfMonthIndex];
            // Prevent invalid day of the month in the arithmetic operation
            int maxMonthDay = iChronology.dayOfMonth().getMaximumValue(instant);
            if (dayOfMonth > maxMonthDay) {
                values[dayOfMonthIndex] = maxMonthDay;
                instant = iChronology.dayOfMonth().set(instant, maxMonthDay);
            }
        }
        
        // Apply the month addition
        instant = iChronology.months().add(instant, valueToAdd);

        // Assemble the new value set from the adjusted instant
        int[] newValues = iChronology.get(partial, instant);
        return newValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}
