public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        // Field not found, add it
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];

        // Determine the insertion point to maintain order
        int i;
        for (i = 0; i < iTypes.length; i++) {
            if (fieldType.getRangeDurationType().getField(iChronology).compareTo(
                iTypes[i].getRangeDurationType().getField(iChronology)) > 0) {
                break;
            }
        }

        // Copy types and values up to the insertion point
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);

        // Insert the new type and value
        newTypes[i] = fieldType;
        newValues[i] = value;

        // Copy the remaining types and values
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);

        // Return a new Partial with the new types and values, after validation
        return new Partial(newTypes, newValues, iChronology);
    } else {
        // Field is already present, set the new value
        if (value == getValue(index)) {
            return this; // No change necessary
        }
        int[] newValues = getValues().clone(); // Clone values to immutable
        newValues[index] = getField(index).set(this, index, newValues, value);
        return new Partial(iTypes, newValues, iChronology); // Create corrected partial
    }
}
