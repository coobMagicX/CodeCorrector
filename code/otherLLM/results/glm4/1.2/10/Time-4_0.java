public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    
    // Intent: Check for the presence and validity of `fieldType` before proceeding.
    if (index != -1 && value == getValue(index)) {
        return this; // Intent: Return the original object if no changes are required.
    }

    // Intent: Ensure proper validation for `fieldType` values before attempting to use them.
    if (!isValidFieldType(fieldType)) {
        throw new IllegalArgumentException("Invalid field type");
    }
    
    DateTimeFieldType[] newTypes = iTypes.clone();
    int[] newValues = iValues.clone();

    // Intent: Verify that the logic for inserting a new DateTimeFieldType into the array maintains the correct order.
    int insertionPoint = index == -1 ? 0 : index;
    if (index == -1) {
        while (insertionPoint < newTypes.length && (newTypes[insertionPoint] != null || newValues[insertionPoint] != Integer.MIN_VALUE)) {
            insertionPoint++;
        }
    }
    
    // Insert the new field type and value at the correct position.
    if (index == -1) { // Add new type
        newTypes[insertionPoint] = fieldType;
        newValues[insertionPoint] = value;
    } else { // Update existing type with new value
        newTypes[index] = fieldType;
        newValues[index] = value;
    }
    
    // Create a new Partial object to ensure full validation.
    Partial newPartial = new Partial(iChronology, newTypes, newValues);
    iChronology.validate(newPartial, newValues); // Intent: Validate the new Partial object.
    
    return newPartial; // Return the newly created or modified Partial object.
}

private boolean isValidFieldType(DateTimeFieldType fieldType) {
    // Implement validation logic for field type if necessary.
    // This could include checks like ensuring the field is supported by iChronology.
    return true;
}