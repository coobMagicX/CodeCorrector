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
        
        // Corrected implementation of adding negative months
        if (valueToAdd < 0) {
            int monthsToRemove = Math.abs(valueToAdd);
            int[] tempValues = values.clone();
            for (int i = 0; i < isize; i++) {
                DateTimeFieldType fieldType = partial.getFieldType(i);
                long instantValue = fieldType.getField(iChronology).get(instant);
                instantValue -= (monthsToRemove * fieldType.getUnitMillis()) / fieldType.getUnitMillisPerYear();
                tempValues[i] = (int) instantValue;
            }
            return add(partial, fieldIndex, tempValues, 0);
        } else {
            instant = add(instant, valueToAdd);
            return iChronology.get(partial, instant);
        }
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}