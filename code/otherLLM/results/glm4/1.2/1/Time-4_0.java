public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    
    // Retrieve existing partial information to manipulate
    DateTimeFieldType[] iTypes = getTypes();
    int[] iValues = getValues();
    Chronology iChronology = getChronology();

    int index = indexOf(fieldType);
    if (index == -1) {
        // Find the correct insertion point
        int i = 0;
        while (i < iTypes.length && iTypes[i] != null) {
            if (fieldType.compareTo(iTypes[i]) <= 0) {
                break;
            }
            i++;
        }

        DateTimeFieldType[] newTypes = Arrays.copyOf(iTypes, i + 1);
        int[] newValues = Arrays.copyOf(iValues, i + 1);

        // Insert the new field type and value at the correct position
        newTypes[i] = fieldType;
        newValues[i] = value;

        // Copy remaining elements if any
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);

        // Create a new Partial object with the updated arrays
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        return newPartial;
    } else if (value == getValue(index)) {
        return this; // Return the existing partial if value is already set for the field type
    } else {
        // Update the existing value of the field type
        int[] newValues = Arrays.copyOf(iValues, iValues.length);
        newValues[index] = value;
        return new Partial(this, newValues); // Assuming this method exists to create a partial with updated values
    }
}