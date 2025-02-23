public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    if (valueToAdd == 0) {
        return values;
    }
    int partialSize = partial.size();
    if (DateTimeUtils.isContiguous(partial) && partial.getFieldType(fieldIndex).getName().equals(DateTimeFieldType.monthOfYear().getName())) {
        long instant = 0L;
        // convert values to an instant to make adjustments
        for (int i = 0; i < partialSize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        
        // add months
        instant = iChronology.months().add(instant, valueToAdd);

        // retrieve the new values
        int[] newValues = iChronology.get(partial, instant);

        // check if day of the month needs adjustment because of month length mismatch
        if (partial.isSupported(DateTimeFieldType.dayOfMonth())) {
            int dayIndex = partial.indexOf(DateTimeFieldType.dayOfMonth());
            int oldDay = values[dayIndex];
            int newDay = newValues[dayIndex];
            int maxDayInNewMonth = iChronology.dayOfMonth().getMaximumValue(instant);

            if (newDay > maxDayInNewMonth) {
                // adjust day of the month to the maximum valid value in the resulting month
                newValues[dayIndex] = maxDayInNewMonth;
            }
        }

        return newValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}
