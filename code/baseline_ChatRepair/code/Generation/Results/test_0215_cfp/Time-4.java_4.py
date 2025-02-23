public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        // Field type is not already present in partial, we need to add a new one
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];

        // Initiate insertion point - default is at the end
        int insertionPoint = iTypes.length;
        DurationField newFieldUnit = fieldType.getDurationType().getField(iChronology);
        
        // Determine the correct insertion point
        for (int i = 0; i < iTypes.length; i++) {
            DurationField currentFieldUnit = iTypes[i].getDurationType().getField(iChronology);
            if (currentFieldUnit.isSupported() && newFieldUnit.isSupported()) {
                if (newFieldUnit.compareTo(currentFieldUnit) > 0) {
                    insertionPoint = i;
                    break;
                } else if (newFieldUnit.compareTo(currentFieldUnit) == 0) {
                    DurationField newFieldRange = fieldType.getRangeDurationType().getField(iChronology);
                    DurationField currentFieldRange = iTypes[i].getRangeDurationType().getField(iChronology);
                    if (newFieldRange.compareTo(currentFieldRange) > 0) {
                        insertionPoint = i;
                        break;
                    }
                }
            }
        }

        // Copy the data up to the insertion point
        System.arraycopy(iTypes, 0, newTypes, 0, insertionPoint);
        System.arraycopy(iValues, 0, newValues, 0, insertionPoint);
        
        // Insert the new field and value
        newTypes[insertionPoint] = fieldType;
        newValues[insertionPoint] = value;
        
        // Copy the remainder of the data
        System.arraycopy(iTypes, insertionPoint, newTypes, insertionPoint + 1, iTypes.length - insertionPoint);
        System.arraycopy(iValues, insertionPoint, newValues, insertionPoint + 1, iValues.length - insertionPoint);

        // Construct a new Partial object
        return new Partial(newTypes, newValues, iChronology);
    } else {
        // Field already exists, update its value
        if (value == getValue(index)) {
            return this; // No change needed
        }
        int[] newValues = getValues().clone();
        newValues[index] = getField(index).set(this, index, newValues, value);
        return new Partial(iTypes, newValues, iChronology);
    }
}
