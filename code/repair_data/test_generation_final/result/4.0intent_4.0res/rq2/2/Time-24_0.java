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
            // Save the default year only if it hasn't been specified by the fields
            boolean yearSpecified = false;
            for (int i = 0; i < count; i++) {
                if (savedFields[i].iField.getType() == DateTimeFieldType.year() ||
                    savedFields[i].iField.getType() == DateTimeFieldType.weekyear()) {
                    yearSpecified = true;
                    break;
                }
            }
            if (!yearSpecified) {
                saveField(DateTimeFieldType.year(), iDefaultYear);
            }
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
            String message = "Illegal instant due to time zone offset transition (" + iZone + ")";
            if (text != null) {
                message = "Cannot parse \"" + text + "\": " + message;
            }
            throw new IllegalArgumentException(message);
        }
    }
    
    return millis;
}