public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFields = savedFields;
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);

    // Set the default year initially to handle cases where year is not specified.
    if (count > 0) {
        boolean hasWeekyear = false;
        for (int i = 0; i < count; i++) {
            DateTimeFieldType fieldType = savedFields[i].iField.getType();
            if (fieldType == DateTimeFieldType.weekyear()) {
                hasWeekyear = true;
                break;
            }
        }
        
        // If the first field is not a month or day or if we have "weekyear", update the year
        DateTimeFieldType firstFieldType = savedFields[0].iField.getType();
        if (hasWeekyear || (firstFieldType == DateTimeFieldType.monthOfYear() || firstFieldType == DateTimeFieldType.dayOfMonth())) {
            // As weekyear can affect the accurate computation of dates, handle it by setting year properly.
            saveField(DateTimeFieldType.year(), iDefaultYear);
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

    if (iZone != null) {
        int offset = iZone.getOffsetFromLocal(millis);
        millis -= offset;
        // Check if the offset has still valid if not throw an error
        if (offset != iZone.getOffset(millis)) {
            String message = "Illegal instant due to time zone offset transition (" + iZone + ")";
            if (text != null) {
                message = "Cannot parse \"" + text + "\": " + message;
            }
            throw new IllegalArgumentException(message);
        }
    } else {
        millis -= iOffset;
    }

    return millis;
}
