public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[])iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);
    if (count > 0) {
        // alter base year for parsing if first field is month or day
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        DurationField first = savedFields[0].iField.getDurationField();
        if (compareReverse(first, months) >= 0 && compareReverse(first, days) <= 0) {
            saveField(DateTimeFieldType.year(), iDefaultYear);
            return computeMillis(resetFields, text); // Fix: Correctly call computeMillis with cloned state
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
    
    if (iZone == null) {
        millis -= iOffset;
    } else {
        Chronology chronology = getChronology(); // Utilize the method to get Chronology
        int offset = iZone.getOffsetFromLocal(millis, chronology);
        millis -= offset;
        if (offset != iZone.getOffset(millis, chronology)) { // Use chronology for getting offset
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