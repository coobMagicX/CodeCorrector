public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
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
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

private long add(long instant, int valueToAdd) {
    if (valueToAdd >= 0) {
        return iChronology.add(instant, valueToAdd);
    } else {
        return minusDays(instant, -valueToAdd);
    }
}

private long minusDays(long instant, int days) {
    MutableDateTime mutableDateTime = new MutableDateTime(instant, iChronology);
    mutableDateTime.addDays(-days);
    return mutableDateTime.getMillis();
}