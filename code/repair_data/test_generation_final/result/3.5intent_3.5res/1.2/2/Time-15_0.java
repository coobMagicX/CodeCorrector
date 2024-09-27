public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    // month is the largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);
        if (DateTimeUtils.isLeapYear(iChronology.year().get(instant)) && iChronology.monthOfYear().get(instant) == 2 && iChronology.dayOfMonth().get(instant) == 29) {
            int newYear = iChronology.year().add(instant, 0);
            int newMonth = iChronology.monthOfYear().add(instant, valueToAdd);
            int newDay = iChronology.dayOfMonth().getMaximumValue(newYear, newMonth);
            return iChronology.get(partial, iChronology.millisOfDay().set(iChronology.dayOfMonth().set(iChronology.monthOfYear().set(instant, newMonth), newDay), 0));
        } else {
            return iChronology.get(partial, instant);
        }
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}