public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        // Clone fields array since original might be modified
        iSavedFields = savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);

    // Specified year related adjustment for processing
    if (count > 0) {
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        DurationField weeks = DurationFieldType.weeks().getField(iChrono);
        DateTimeFieldType firstFieldType = savedFields[0].iField.getType();
        DurationField first = savedFields[0].iField.getDurationField();
        
        if (DateTimeFieldType.weekyear().equals(firstFieldType)) {
           saveField(DateTimeFieldType.weekyear(), iDefaultYear);
        } else if (compareReverse(first, months) >= 0 && compareReverse(first, days) <= 0) {
           // If first field is a month or day (and not weekyear), set year to default
           saveField(DateTimeFieldType.year(), iDefaultYear);
        }
    }

    long millis = iMillis;
    // Attempt to set each field
    try {
        for (int i = 0; i < count; i++) {
            millis = savedFields[i].set(millis, resetFields);
        }
    } catch (IllegalFieldValueException e) {
        if (text != null) {
            e.prependMessage("Cannot parse \"" + text + '"');
        }
        throw e;
    }

    // Adjust for time zone if necessary
    if (iZone == null) {
        millis -= iOffset;
    } else {
        int offset = iZone.getOffsetFromLocal(millis);
        millis -= offset;
        if (offset != iZone.getOffset(millis)) {
            String message = "Illegal instant due to time zone offset transition (" + iZone + ')';
            if (text != null) {
                message = "Cannot parse \"" + text + "\": " + message;
            }
            throw new IllegalArgumentException(message);
        }
    }
    
    return millis;
}
