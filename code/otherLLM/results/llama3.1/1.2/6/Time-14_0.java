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
        
        // Handle negative months
        if (valueToAdd < 0) {
            instant = subtractMonths(instant, -valueToAdd);
        } else {
            instant = add(instant, valueToAdd);
        }
        
        return iChronology.get(partial, instant);
    } else {
        // Ensure correct handling of edge cases such as adding 0 months
        if (valueToAdd == 0) {
            return values;
        }
        
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}