public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }

    // Finds the existing index of the field type, or returns -1 if not found.
    int index = indexOf(fieldType);

    // If fieldType is not present in iTypes.
    if (index == -1) {
        int newSize = iTypes.length + 1;
        DateTimeFieldType[] newTypes = new DateTimeFieldType[newSize];
        int[] newValues = new int[newSize];

        DurationField newFieldDuration = fieldType.getDurationType().getField(iChronology);
        boolean placed = false;

        for (int i = 0, j = 0; i < iTypes.length; i++, j++) {
            DurationField existingFieldDuration = iTypes[i].getDurationType().getField(iChronology);
            if (!placed && newFieldDuration.compareTo(existingFieldDuration) > 0) {
                newTypes[j] = fieldType;
                newValues[j] = value;
                placed = true;
                j++;
            }
            newTypes[j] = iTypes[i];
            newValues[j] = iValues[i];
        }

        if (!placed) {
            newTypes[newSize - 1] = fieldType;
            newValues[newSize - 1] = value;
        }

        return new Partial(iChronology, newTypes, newValues);
    } else {
        // If field type is already present in the iTypes.
        if (getValue(index) == value) {
            return this; // No change needed
        }

        // Changing the value for an existing field.
        int[] newValues = getValues().clone();
        newValues[index] = value;
        return new Partial(iChronology, iTypes, newValues);
    }
}
