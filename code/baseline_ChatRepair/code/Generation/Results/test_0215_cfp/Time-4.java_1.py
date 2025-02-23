public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        // Field not currently part of this Partial
        int newSize = iTypes.length + 1;
        DateTimeFieldType[] newTypes = new DateTimeFieldType[newSize];
        int[] newValues = new int[newSize];
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        
        int insertPos;
        for (insertPos = 0; insertPos < iTypes.length; insertPos++) {
            DateTimeFieldType existingType = iTypes[insertPos];
            DurationField existingUnitField = existingType.getDurationType().getField(iChronology);
            if (unitField.compareTo(existingUnitField) > 0) {
                break; // found the correct position to insert the new field
            }
        }

        // Copy types/values to the new array before and after the insertion point
        System.arraycopy(iTypes, 0, newTypes, 0, insertPos);
        System.arraycopy(iValues, 0, newValues, 0, insertPos);
        newTypes[insertPos] = fieldType;
        newValues[insertPos] = value;
        System.arraycopy(iTypes, insertPos, newTypes, insertPos + 1, iTypes.length - insertPos);
        System.arraycopy(iValues, insertPos, newValues, insertPos + 1, iValues.length - insertPos);

        // Create a new Partial instance with the adjusted types and values
        return new Partial(iChronology, newTypes, newValues);
    } else {
        // Field already present, update value
        if (value == getValue(index)) {
            return this; // no change to value
        }
        int[] newValues = getValues().clone();
        newValues[index] = value;
        return new Partial(iChronology, iTypes, newValues);
    }
}
