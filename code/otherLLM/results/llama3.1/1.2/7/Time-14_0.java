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
        
        // Check if valueToAdd is negative and add a special case to handle contiguous dates correctly
        if (valueToAdd < 0) {
            int monthsToSubtract = Math.abs(valueToAdd);
            for (int i = isize - 1; i >= 0; i--) {
                instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
                
                // Subtract months from the day field
                int daysToSubtract = partial.getFieldType(i).getDaysInMonth(values[i], iChronology);
                values[i] -= Math.min(daysToSubtract, monthsToSubtract);
                if (values[i] < 0) {
                    values[i] += daysToSubtract;
                    instant = add(instant, -daysToSubtract);
                    monthsToSubtract -= daysToSubtract;
                }
            }
        } else {
            instant = add(instant, valueToAdd);
        }
        
        return iChronology.get(partial, instant);
    } else {
        // Check if the addition will result in a date that is not contiguous with the original
        long newInstant = add(0L, valueToAdd);
        ReadablePartial newPartial = iChronology.get(newInstant);
        
        // If the resulting partial is not contiguous, use the superclass method to handle it
        if (!DateTimeUtils.isContiguous(newPartial)) {
            return super.add(partial, fieldIndex, values, valueToAdd);
        }
        
        // Otherwise, continue with the original logic
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = newPartial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        
        return iChronology.get(partial, instant);
    }
}