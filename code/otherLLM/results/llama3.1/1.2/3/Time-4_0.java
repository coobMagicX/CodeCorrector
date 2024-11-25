public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        // find correct insertion point to keep largest-smallest order
        int i = 0;
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        if (unitField.isSupported()) {
            for (; i < iTypes.length; i++) {
                DateTimeFieldType loopType = iTypes[i];
                DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
                if (loopUnitField.isSupported()) {
                    int compare = unitField.compareTo(loopUnitField);
                    if (compare > 0) {
                        break;
                    } else if (compare == 0) {
                        DurationField rangeField = fieldType.getRangeDurationType().getField(iChronology);
                        DurationField loopRangeField = loopType.getRangeDurationType().getField(iChronology);
                        if (rangeField.compareTo(loopRangeField) > 0) {
                            break;
                        }
                    }
                }
            }
        }
        
        // Fix potential off-by-one error and validate array operations
        System.arraycopy(iTypes, 0, newTypes, 0, i + 1);  // Use i + 1 to avoid losing existing types
        
        System.arraycopy(iValues, 0, newValues, 0, i + 1);
        
        newTypes[i] = fieldType;
        newValues[i] = value;
        
        // Fix potential off-by-one error and validate array operations
        int remainingTypes = newTypes.length - (i + 1);
        System.arraycopy(iTypes, i + 1, newTypes, i + 2, remainingTypes);  // Use i + 1 to avoid losing existing types
        
        int remainingValues = newValues.length - (i + 1);
        System.arraycopy(iValues, i + 1, newValues, i + 2, remainingValues);
        
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    }
    if (value == getValue(index)) {
        return this;
    }
    
    // Fix potential off-by-one error
    int[] newValues = getValues();
    newValues = getField(index).set(this, index, newValues, value);
    return new Partial(this, newValues);
}