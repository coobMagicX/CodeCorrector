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

    // Handle specific fields for week years if necessary
    if (count > 0 && isSupported(DateTimeFieldType.weekyear())) {
        // Determine if the largest duration field is a weekyear field
        DurationField largest = savedFields[0].iField.getDurationField();
        if (largest.getType() == DurationFieldType.weekyears()) {
            DurationField weeks = DurationFieldType.weeks().getField(iChrono);
            // Set the provisional date to the first week of the given weekyear
            saveField(DateTimeFieldType.year(), iDefaultYear);
            saveField(DateTimeFieldType.weekyear(), savedFields[0].iValue);
            saveField(DateTimeFieldType.weekOfWeekyear(), 1);
            saveField(DateTimeFieldType.dayOfWeek(), 1);  // ISO-8601, Monday is the first day of the week
            count = iSavedFieldsCount;  // Update count as new fields might have been saved
        }
    }

    // Compute millis
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

    // Account for time zone
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
