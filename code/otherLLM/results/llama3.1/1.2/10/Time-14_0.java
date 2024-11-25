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
            if (i == fieldIndex) {
                instant = add(instant, values[i], valueToAdd);
            } else {
                instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
            }
        }
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

private long add(long instant, int fieldValue, int months) {
    return Instant.from(iChronology.getInstant(instant)).plusMonths(months).toEpochMilli();
}