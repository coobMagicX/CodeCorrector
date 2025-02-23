public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFields = savedFields;
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);

    // Check if the first field is weekyear, if so, adjust default setting to weekyear
    if (count > 0 && savedFields[0].iField.getType() == DateTimeFieldType.weekyear()) {
        saveField(DateTimeFieldType.weekyear(), iDefaultYear);
        return computeMillis(resetFields, text); 
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
