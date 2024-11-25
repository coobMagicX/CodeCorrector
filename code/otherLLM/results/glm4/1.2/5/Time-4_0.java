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
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);
        newTypes[i] = fieldType;
        newValues[i] = value;
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);
        
        // Use the withField method to create a new Partial without modifying original values
        return withField(fieldType, value).createNewPartial(newTypes, newValues);
    }
    if (value == getValue(index)) {
        return this;
    }
    int[] newValues = getValues();
    newValues[index] = value; // Update the existing value instead of creating a new array
    return new Partial(this, newValues);
}

// Assuming there is a method to create a new Partial instance with given types and values
private Partial createNewPartial(DateTimeFieldType[] newTypes, int[] newValues) {
    return new Partial(iChronology, newTypes, newValues); // This needs to be defined somewhere in the class
}