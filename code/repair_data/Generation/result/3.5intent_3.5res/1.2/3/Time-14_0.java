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
        
        if (DateTimeUtils.isLeapYear(iChronology.year().get(instant)) &&
                partial.getFieldType(0).getDurationType().getName().equals("months") &&
                partial.getFieldType(0).getField(iChronology).getMaximumValue() == 29) {
            instant = handleLeapYearMonthSubtraction(instant, valueToAdd);
        }
        
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

private long handleLeapYearMonthSubtraction(long instant, int valueToAdd) {
    DateTimeField monthField = iChronology.monthOfYear();
    int month = monthField.get(instant);
    
    for (int i = 0; i < Math.abs(valueToAdd); i++) {
        if (valueToAdd > 0) {
            month = monthField.add(month, 1);
            if (month == 3) {
                instant = iChronology.dayOfMonth().set(instant, 28); // Adjust to last day of February in non-leap years
            }
        } else {
            month = monthField.add(month, -1);
            if (month == 1) {
                instant = iChronology.dayOfMonth().set(instant, 31); // Adjust to last day of January
            }
        }
    }
    
    instant = iChronology.monthOfYear().set(instant, month);
    
    return instant;
}