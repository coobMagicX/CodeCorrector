public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    
    // Assuming the existence of a method indexOf to find the index of the DateTimeFieldType
    int index = indexOf(fieldType);
    if (index == -1) { // This is where the repair needs to happen
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        
        // Find correct insertion point to keep largest-smallest order
        int i = 0;
        while (i < iTypes.length && fieldType.compareTo(iTypes[i]) >= 0) {
            i++;
        }
        
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);
        newTypes[i] = fieldType;
        newValues[i] = value;
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);
        
        // Use public constructor to ensure full validation
        Partial newPartial = new Partial(iChronology, newTypes, newValues);
        iChronology.validate(newPartial, newValues); // Assuming there is a method validate in iChronology
        return newPartial;
    } else {
        if (value == getValue(index)) {
            return this;
        }
        
        int[] newValues = getValues();
        newValues[index] = value; // Setting the value directly instead of using getField.set
        
        // Assuming there's a constructor that takes iChronology, newValues, and returns Partial
        return new Partial(this, newValues);
    }
}