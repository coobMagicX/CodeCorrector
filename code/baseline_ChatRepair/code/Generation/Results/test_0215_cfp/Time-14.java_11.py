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

        // Find the index of DayOfMonth if it exists
        int dayOfMonthIndex = -1;
        for (int i = 0; i < partial.size(); i++) {
            if (partial.getFieldType(i) == DateTimeFieldType.dayOfMonth()) {
                dayOfMonthIndex = i;
                break;
            }
        }

        if (partial.getFieldType(fieldIndex) == DateTimeFieldType.monthOfYear() && dayOfMonthIndex != -1) {
            // We need to ensure the day of month is valid after adjustment
            // Get the max day of the month value for the new month
            DateTimeField dayField = partial.getFieldType(dayOfMonthIndex).getField(iChronology);
            int maxDayOfMonth = dayField.getMaximumValue(instant);
            if (values[dayOfMonthIndex] > maxDayOfMonth) {
                // If original day of month exceeds new max, adjust day to max allowable
                values[dayOfMonthIndex] = maxDayOfMonth;
                instant = dayField.set(instant, maxDayOfMonth);
            }
        }
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}
