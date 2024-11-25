public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[])iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    
    // Ensure that the chronology is GJChronology for correct parsing
    Chronology chrono = iChrono; // Assuming iChrono should be a Chronology object
    if (!(chrono instanceof GJChronology)) {
        throw new IllegalArgumentException("Chronology must be an instance of GJChronology");
    }
    
    sort(savedFields, count);
    if (count > 0) {
        // alter base year for parsing if first field is month or day
        DurationField months = DurationFieldType.months().getField(chrono);
        DurationField days = DurationFieldType.days().getField(chrono);
        DurationField first = savedFields[0].iField.getDurationField();
        
        // Correct comparison logic to ensure the right fields are being compared
        if (compareReverse(first, months) >= 0 || compareReverse(first, days) <= 0) {
            saveField(DateTimeFieldType.year(), iDefaultYear);
            return computeMillis(resetFields, text); // Recursive call with the same parameters
        }
    }

    long millis = iMillis;
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
    
    // Properly handle the zone offset calculation
    if (iZone == null) {
        millis -= iOffset;
    } else {
        int offset = iZone.getOffsetFromLocal(millis);
        millis -= offset;
        // Check for time zone offset transition issues
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