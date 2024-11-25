public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    
    // Assuming the existence of indexOf and indexOfSupported methods
    int index = indexOf(fieldType);
    if (index == -1) {
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        System.arraycopy(iTypes, 0, newTypes, 0, iTypes.length);
        System.arraycopy(iValues, 0, newValues, 0, iValues.length);

        // Find the correct insertion point to maintain largest-smallest order
        for (int i = 0; i < newTypes.length - 1; i++) {
            if (fieldType.compareTo(newTypes[i]) <= 0) {
                System.arraycopy(newTypes, i + 1, newTypes, i + 2, newTypes.length - i - 2);
                newTypes[i] = fieldType;
                break;
            }
        }

        // Insert the new field type and value
        int insertionPoint = indexOfSupported(fieldType);
        newValues[insertionPoint] = value;

        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    } else {
        DateTimeFieldType[] newTypes = iTypes.clone();
        int[] newValues = iValues.clone();

        // Replace the existing value with the new one
        newValues[index] = value;

        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    }
}