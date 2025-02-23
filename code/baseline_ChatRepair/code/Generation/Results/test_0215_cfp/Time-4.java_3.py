public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        // find correct insertion point to keep largest-smallest order
        int insertionPoint = 0;
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        while (insertionPoint < iTypes.length) {
            DurationField loopUnitField = iTypes[insertionPoint].getDurationType().getField(iChronology);
            if (loopUnitField.isSupported()) {
                int compare = unitField.compareTo(loopUnitField);
                if (compare > 0) {
                    break;
                } else if (compare == 0) {
                    DurationField rangeField = fieldType.getRangeDurationType().getField(iChronology);
                    DurationField loopRangeField = iTypes[insertionPoint].getRangeDurationType().getField(iChronology);
                    if (rangeField.compareTo(loopRangeField) > 0) {
                        break;
                    }
                }
            }
            insertionPoint++;
        }
        
        System.arraycopy(iTypes, 0, newTypes, 0, insertionPoint);
        System.arraycopy(iValues, 0, newValues, 0, insertionPoint);
        newTypes[insertionPoint] = fieldType;
        newValues[insertionPoint] = value;
        System.arraycopy(iTypes, insertionPoint, newTypes, insertionPoint + 1, iTypes.length - insertionPoint);
        System.arraycopy(iValues, insertionPoint, newValues, insertionPoint + 1, iValues.length - insertionPoint);
        
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    } else {
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = getValues();
        newValues[index] = getField(index).set(this, index, newValues, value);
        return new Partial(this, newValues);
    }
}
