public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;

    // Clone fields if shared
    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFieldsShared = false;
    }

    // Sort the fields
    sort(savedFields, count);

    // Initialize base millis
    long millis = iMillis;

    if (count > 0) {
        // Check if the first field is 'weekyear'
        if (savedFields[0].iField.getType() == DateTimeFieldType.weekyear()) {
            int weekyear = savedFields[0].iValue;
            // Temporarily adjust the default year to handle weekyear parsing
            saveField(DateTimeFieldType.year(), weekyear);
            // Reset fields count
            count = iSavedFieldsCount;
        }
    }

    // Set fields
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

    // Adjust for time zone offset
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
