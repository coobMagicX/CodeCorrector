public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }

    int index = indexOf(fieldType);
    if (index == -1) {
        // Field not present, preparing to add
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];

        int i = 0;
        boolean insert = false;
        for (; i < iTypes.length; i++) {
            DurationField existingUnitField = iTypes[i].getDurationType().getField(iChronology);
            DurationField newUnitField = fieldType.getDurationType().getField(iChronology);

            if (!insert && newUnitField.isSupported() && existingUnitField.isSupported()) {
                int comparison = newUnitField.compareTo(existingUnitField);
                if (comparison > 0) {
                    insert = true;
                    break;
                } else if (comparison == 0) {
                    // if both fields have the same duration, compare range duration
                    DurationField existingRangeField = iTypes[i].getRangeDurationType().getField(iChronology);
                    DurationField newRangeField = fieldType.getRangeDurationType().getField(iChronology);
                    if (!newRangeField.isSupported() || existingRangeField.isSupported() &&
                        newRangeField.compareTo(existingRangeField) > 0) {
                        insert = true;
                        break;
                    }
                }
            }
        }

        // Insert the new field type and value in the calculated position
        if (insert) {
            System.arraycopy(iTypes, 0, newTypes, 0, i);
            System.arraycopy(iValues, 0, newValues, 0, i);
        }

        newTypes[i] = fieldType;
        newValues[i] = value;

        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);

        // validate changes and return new instance
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    } else {
        // Field present, set new value
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues().clone();
        newValues[index] = value;
        return new Partial(iChronology, iTypes, newValues);
    }
}
