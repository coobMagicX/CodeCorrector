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
            instant = addFields(instant, values[i], iChronology.getFieldType(i));
        }
        instant += valueToAdd * iChronology.getAverageMillisPerMonth();
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

private long addFields(long instant, int value, DateTimeFieldType fieldType) {
    switch (fieldType) {
        case year:
            instant += value * iChronology.getAverageMillisPerYear();
            break;
        case monthOfYear:
            instant += value * iChronology.getAverageMillisPerMonth();
            break;
        // add cases for other fields like dayOfMonth, etc.
        default:
            throw new IllegalArgumentException("Unsupported field type");
    }
    return instant;
}