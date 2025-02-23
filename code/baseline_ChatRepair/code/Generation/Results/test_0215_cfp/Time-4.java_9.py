public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    
    int index = indexOf(fieldType);
    if (index == -1) {
        // Field not present, add new field
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        // Determine correct insertion point to maintain field order
        int insertAt = 0;
        DurationField newFieldDuration = fieldType.getDurationType().getField(iChronology);
        while (insertAt < iTypes.length && newFieldDuration.isSupported() && 
               newFieldDuration.compareTo(iTypes[insertAt].getDurationType().getField(iChronology)) > 0) {
            insertAt++;
        }
        
        // Copy existing fields and values up to insert point
        System.arraycopy(iTypes, 0, newTypes, 0, insertAt);
        System.arraycopy(iValues, 0, newValues, 0, insertAt);
        
        // Insert new field and value
        newTypes[insertAt] = fieldType;
        newValues[insertAt] = value;
        
        // Copy remaining fields and values
        System.arraycopy(iTypes, insertAt, newTypes, insertAt + 1, iTypes.length - insertAt);
        System.arraycopy(iValues, insertAt, newValues, insertAt + 1, iValues.length - insertAt);
        
        // Create the new Partial object with updated types and values
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    } else {
        // Field already present, update value if necessary
        if (value == getValue(index)) {
            return this; // Return same instance if value unchanged
        }
        // Update value and create new Partial instance
        int[] newValues = getValues().clone();
        newValues[index] = value;
        return new Partial(iChronology, iTypes, newValues);
    }
}
