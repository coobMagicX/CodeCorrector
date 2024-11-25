public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];

        // Initialize the new array with the current values
        System.arraycopy(iTypes, 0, newTypes, 0, iTypes.length);
        System.arraycopy(iValues, 0, newValues, 0, iValues.length);

        // find correct insertion point to keep largest-smallest order
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

        // Insert the new field and value
        newTypes[i] = fieldType;
        newValues[i] = value;

        // Copy remaining elements to maintain array size
        System.arraycopy(iTypes, i, newTypes, i + 1, newTypes.length - i - 1);
        System.arraycopy(iValues, i, newValues, i + 1, newValues.length - i - 1);

        // use public constructor to ensure full validation
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    } else {
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues();
        newValues[index] = value; // Set the value directly without creating a new Partial object

        // Validate the changes before returning
        iChronology.validate(this, newValues);
        return new Partial(iChronology, iTypes, newValues); // Create a new Partial with updated values
    }
}