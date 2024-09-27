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
        
        // Adjust the day component if necessary
        int[] adjustedValues = iChronology.get(partial, instant);
        int adjustedDay = adjustedValues[DateTimeFieldType.dayOfMonth().getRangeDurationField().getType()];
        int originalDay = values[DateTimeFieldType.dayOfMonth().getRangeDurationField().getType()];
        
        if (adjustedDay != originalDay) {
            int lastValidDay = DateTimeFieldType.dayOfMonth().getMaximumValue(instant);
            adjustedValues[DateTimeFieldType.dayOfMonth().getRangeDurationField().getType()] = lastValidDay;
        }
        
        return adjustedValues;
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}