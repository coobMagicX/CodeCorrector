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
        if (partial.get(DateTimeFieldType.monthOfYear()) == 2 && partial.get(DateTimeFieldType.dayOfMonth()) == 29 && iChronology.isLeapYear(partial.get(DateTimeFieldType.year()))) {
            int newMonth = partial.get(DateTimeFieldType.monthOfYear()) + valueToAdd;
            int newYear = partial.get(DateTimeFieldType.year());
            if (newMonth > 12) {
                newYear += newMonth / 12;
                newMonth = newMonth % 12;
            }
            if (newMonth == 2 && newYear % 4 != 0) {
                values[fieldIndex] = 28; // Adjust to last day of February in non-leap years
            } else {
                values[fieldIndex] = newMonth;
            }
        } else {
            instant = add(instant, valueToAdd);
            return iChronology.get(partial, instant);
        }
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}