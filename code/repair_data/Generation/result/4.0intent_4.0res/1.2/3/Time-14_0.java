public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);

        // Adjust the day of month if necessary (e.g., February 29 in a non-leap year)
        int[] newValues = iChronology.get(partial, instant);
        if (partial.getChronology().isLeapYear(newValues[0]) || newValues[1] != 2 || newValues[2] <= 28) {
            return newValues;
        } else if (newValues[2] > 28) {
            newValues[2] = partial.getChronology().dayOfMonth().getMaximumValue(new long[]{newValues[0], newValues[1]});
        }
        return newValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}