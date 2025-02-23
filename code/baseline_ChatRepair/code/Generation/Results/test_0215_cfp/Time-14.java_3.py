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

        // Ensure the correct last day of month adjustment
        if (fieldIndex == partial.indexOf(DateTimeFieldType.monthOfYear()) && 
            partial.isSupported(DateTimeFieldType.dayOfMonth())) {
            try {
                return iChronology.get(partial, instant);
            } catch (IllegalFieldValueException ex) {
                // We hit an illegal day of the month for the resulting month, adjust day downwards
                int dayIndex = partial.indexOf(DateTimeFieldType.dayOfMonth());
                if (dayIndex >= 0) {
                    int lastDayOfMonth = iChronology.dayOfMonth().getMaximumValue(instant);
                    values[dayIndex] = lastDayOfMonth;
                    instant = partial.getFieldType(dayIndex).getField(iChronology).set(instant, lastDayOfMonth);
                }
                return iChronology.get(partial, instant);
            }
        } else {
            return iChronology.get(partial, instant);
        }
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}
