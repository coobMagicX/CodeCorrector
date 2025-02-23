public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        // Expanding arrays to include the new field and value
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];

        // Find correct insertion point to keep fields in order
        int i = 0;
        for (; i < iTypes.length; i++) {
            if (fieldType.getDurationType().getField(iChronology).compareTo(iTypes[i].getDurationType().getField(iChronology)) > 0) {
                break;
            }
        }

        // Copy over parts of the old arrays to the new arrays
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);
        newTypes[i] = fieldType;
        newValues[i] = value;
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);

        // Validate and return new Partial
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    } else {
        // If field is already present, just set the new value
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues().clone(); // Ensure we are working on a clone for immutability
        newValues[index] = getField(index).set(this, index, newValues, value);
        return new Partial(this, newValues);
    }
}
