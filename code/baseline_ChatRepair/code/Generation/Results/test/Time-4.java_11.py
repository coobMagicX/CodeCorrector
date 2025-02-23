public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }

    int index = indexOf(fieldType);
    if (index == -1) {
        // Field not present, need to add it properly
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        // Determine proper insertion point
        int i;
        for (i = 0; i < iTypes.length; i++) {
            if (fieldType.compareTo(iTypes[i]) < 0) {
                break;
            }
        }

        // Copy part before the insertion point
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);

        // Insert the new field type and value
        newTypes[i] = fieldType;
        newValues[i] = value;

        // Copy part after the insertion point
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);

        // Create a new Partial with the new types and values
        return new Partial(iChronology, newTypes, newValues);
    } else {
        // Field already present, check for same value to return 'this'
        if (value == getValue(index)) {
            return this;
        }

        // Value different, update it in a new values array
        int[] newValues = getValues().clone();
        newValues[index] = value;
        // Return the new Partial
        return new Partial(iChronology, iTypes, newValues);
    }
}
