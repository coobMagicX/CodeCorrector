public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        // Create new arrays to hold the new type and values
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[iValues.length + 1];
        
        // Find the insertion index to maintain the order
        int insertionIndex = 0;
        for (; insertionIndex < iTypes.length; insertionIndex++) {
            if (iChronology.fieldComparator().compare(fieldType, iTypes[insertionIndex]) < 0) {
                break;
            }
        }
        
        // Copy old types and values into new arrays up to the insertion index
        System.arraycopy(iTypes, 0, newTypes, 0, insertionIndex);
        System.arraycopy(iValues, 0, newValues, 0, insertionIndex);
        
        // Insert the new field type and value
        newTypes[insertionIndex] = fieldType;
        newValues[insertionIndex] = value;
        
        // Copy the remaining old types and values into new arrays after the insertion index
        System.arraycopy(iTypes, insertionIndex, newTypes, insertionIndex + 1, iTypes.length - insertionIndex);
        System.arraycopy(iValues, insertionIndex, newValues, insertionIndex + 1, iValues.length - insertionIndex);
        
        // Create a new Partial with the updated arrays
        return new Partial(newTypes, newValues, iChronology);
    } else {
        // Field already exists, simply set new value
        if (value == getValue(index)) {
            return this; // No change needed
        }
        // Clone the existing values array and set the new value
        int[] newValues = iValues.clone();
        newValues[index] = getField(index).set(this, index, newValues, value);
        
        // Create a new Partial with the updated value
        return new Partial(iTypes, newValues, iChronology);
    }
}
