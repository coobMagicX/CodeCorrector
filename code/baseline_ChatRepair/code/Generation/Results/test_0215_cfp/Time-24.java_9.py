public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    
    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    
    sort(savedFields, count);
    if (count > 0) {
        DurationField years = DurationFieldType.years().getField(iChrono);
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        DurationField first = savedFields[0].iField.getDurationField();
        
        if (savedFields[0].iField.getType() == DateTimeFieldType.weekyear()) {
            saveField(DateTimeFieldType.year(), iDefaultYear);
            saveField(DateTimeFieldType.weekyear(), savedFields[0].iValue);
            count = iSavedFieldsCount;  // re-read count value as it may have changed
        } else if (compareReverse(first, months) >= 0 && compareReverse(first, days) <= 0) {
            // alter base year for parsing if first field is month or day
            saveField(DateTimeFieldType.year(), iDefaultYear);
            count = iSavedFieldsCount;  // re-read count value as it may have changed
        }
    }

    long millis = iMillis;
    try {
        for (int i = 0; i < count; i++) {
            millis = savedFields[i].set(millis, resetFields);
        }
    } catch (IllegalFieldValueException e) {
        if (text != null) {
            e.prependMessage("Cannot parse \"" + text + "\"");
        }
        throw e;
    }
    
    if (iZone == null) {
        millis -= iOffset;
    } else {
        int offset = iZone.getOffsetFromLocal(millis);
        millis -= offset;
        if (offset != iZone.getOffset(millis)) {
            String message =
                "Illegal instant due to time zone offset transition (" + iZone + ')';
            if (text != null) {
                message = "Cannot parse \"" + text + "\": " + message;
            }
            throw new IllegalArgumentException(message);
        }
    }
    
    return millis;
}
