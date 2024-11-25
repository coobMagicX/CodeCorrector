public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        // find correct insertion point to keep largest-smallest order
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        if (unitField.isSupported()) {
            for (int i = 0; i < iTypes.length; i++) {
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
        System.arraycopy(iTypes, 0, newTypes, 0, iTypes.length);
        System.arraycopy(iValues, 0, newValues, 0, iValues.length);
        newTypes[iTypes.length] = fieldType;
        newValues[iTypes.length] = value;
        // use public constructor to ensure full validation
        Partial newPartial = new Partial(this, newTypes, newValues);
        return newPartial;
    }
    if (value < getValue(index) || value > getValue(index)) {
        throw new IllegalArgumentException("The value must be within the valid range for the given field type");
    }
    int[] newValues = getValues();
    newValues[index] = value;
    return new Partial(this, newValues);
}