public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    
    // If the fieldType is clockhourOfDay, handle it separately since its duration unit is not directly comparable to other types.
    if (DateTimeFieldType.clockhourOfDay().equals(fieldType)) {
        if (value != getValue(index)) { // Check if the value has changed
            int[] newValues = getValues();
            if (index == -1) { // fieldType is not present in iTypes
                DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
                System.arraycopy(iTypes, 0, newTypes, 0, index);
                newTypes[index] = fieldType;
                System.arraycopy(iValues, 0, newValues, 0, index);
                newValues[index] = value;
                System.arraycopy(iTypes, index, newTypes, index + 1, iTypes.length - index);
                System.arraycopy(iValues, index, newValues, index + 1, iValues.length - index);
            } else {
                // Update the existing field type and value
                DateTimeFieldType[] newTypes = iTypes.clone();
                int[] newValues = getValues().clone();
                newTypes[index] = fieldType;
                newValues[index] = value;
            }
            return new Partial(iChronology, newTypes, newValues);
        } else {
            // No change is needed
            return this;
        }
    }

    if (index == -1) { // fieldType is not present in iTypes
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];

        // Find the correct insertion point to keep the largest-to-smallest order.
        int i = 0;
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        if (unitField.isSupported()) {
            for (; i < iTypes.length; i++) {
                DateTimeFieldType loopType = iTypes[i];
                DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
                if (loopUnitField.isSupported()) {
                    int compare = unitField.compareTo(loopUnitField);
                    if (compare > 0) {
                        break;
                    } else if (compare == 0) {
                        DurationField rangeField = fieldType.getRangeDurationType().getField(iChronology);
                        DurationField loopRangeField = loopType.getRangeDurationType().getField(iChronology);
                        if (rangeField.compareTo(loopRangeField) > 0) {
                            break;
                        }
                    }
                }
            }
        }
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);
        newTypes[i] = fieldType;
        newValues[i] = value;
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);

        // Use public constructor to ensure full validation
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    } else if (value == getValue(index)) {
        return this;
    } else {
        int[] newValues = getValues().clone();
        newValues[index] = value; // Update the existing field type and value
        return new Partial(this, newValues); // Assuming there's a constructor that takes the current instance and new values.
    }
}