public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    
    // Check if the field type already exists and has the correct value
    int index = indexOf(fieldType);
    if (index != -1 && value == getValue(index)) {
        return this; // The Partial object remains unmodified
    }

    // If the field type does not exist, insert it in the correct order
    int insertionIndex = index == -1 ? iTypes.length : index;
    for (int i = 0; i < iTypes.length; i++) {
        DateTimeFieldType existingType = iTypes[i];
        DurationField existingUnitField = existingType.getDurationType().getField(iChronology);
        DurationField unitField = fieldType.getDurationType().getField(iChronology);

        if (!unitField.isSupported()) {
            continue;
        }

        int compare = existingUnitField.compareTo(unitField);
        if (compare > 0) {
            insertionIndex = i; // Found a type with a larger duration field
            break;
        } else if (compare == 0) {
            DurationField existingRangeField = existingType.getRangeDurationType().getField(iChronology);
            DurationField rangeField = fieldType.getRangeDurationType().getField(iChronology);

            int compareRange = existingRangeField.compareTo(rangeField);
            if (compareRange > 0 || (compare == 0 && value < getValue(index))) {
                insertionIndex = i; // Found a type with the same duration field but larger range or smaller value
                break;
            }
        }
    }

    // Create new arrays for types and values to insert the new field type
    DateTimeFieldType[] newTypes = Arrays.copyOf(iTypes, iTypes.length + 1);
    int[] newValues = Arrays.copyOf(iValues, iValues.length + 1);

    // Insert the new field type and value at the correct position
    System.arraycopy(newTypes, insertionIndex, newTypes, insertionIndex + 1, newTypes.length - insertionIndex - 1);
    System.arraycopy(iValues, insertionIndex, newValues, insertionIndex + 1, iValues.length - insertionIndex - 1);

    newTypes[insertionIndex] = fieldType;
    newValues[insertionIndex] = value;

    // Create a new Partial object with the updated types and values
    Partial newPartial = new Partial(iChronology, newTypes, newValues);
    iChronology.validate(newPartial, newValues); // Validate the new partial after modification

    return newPartial;
}