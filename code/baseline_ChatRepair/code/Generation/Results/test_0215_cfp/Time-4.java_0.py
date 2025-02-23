public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        // Field not present, need to add it
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        int insertPos = 0;
        DurationField newFieldUnit = fieldType.getDurationType().getField(iChronology);
        while (insertPos < iTypes.length) {
            DurationField existingFieldUnit = iTypes[insertPos].getDurationType().getField(iChronology);
            // Compare the units to determine the insertion point
            if (newFieldUnit.compareTo(existingFieldUnit) > 0) {
                break;
            } else if (newFieldUnit.compareTo(existingFieldUnit) == 0) {
                // If equal, resolve by range duration type
                DurationField newFieldRange = fieldType.getRangeDurationType().getField(iChronology);
                DurationField existingFieldRange = iTypes[insertPos].getRangeDurationType().getField(iChronology);
                if (newFieldRange.compareTo(existingFieldRange) > 0) {
                    break;
                }
            }
            insertPos++;
        }
        
        // Copy original fields and values into new arrays with space for the new field
        System.arraycopy(iTypes, 0, newTypes, 0, insertPos);
        System.arraycopy(iValues, 0, newValues, 0, insertPos);
        newTypes[insertPos] = fieldType;
        newValues[insertPos] = value;
        System.arraycopy(iTypes, insertPos, newTypes, insertPos + 1, iTypes.length - insertPos);
        System.arraycopy(iValues, insertPos, newValues, insertPos + 1, iValues.length - insertPos);
        
        // Create new Partial with the expanded arrays
        return new Partial(iChronology, newTypes, newValues);
    } else {
        // Field is present, just need to set the new value
        if (value == getValue(index)) {
            return this; // Value unchanged, return same partial
        }
        int[] newValues = getValues().clone(); // Clone the values array
        newValues[index] = value; // Set the new value
        // Return new Partial with modified values
        return new Partial(iChronology, iTypes, newValues);
    }
}
