public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;

    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    // Sort fields, which helps in handling dependant fields like day and month correctly
    sort(savedFields, count);

    long millis = iMillis;
    try {
        if (count > 0) {
            DurationField months = DurationFieldType.months().getField(iChrono);
            DurationField days = DurationFieldType.days().getField(iChrono);
            DurationField firstDurationField = savedFields[0].iField.getDurationField();

            // Check for month or day field being the first and setting default year appropriately
            if (compareReverse(firstDurationField, months) >= 0 && compareReverse(firstDurationField, days) <= 0) {
                saveField(DateTimeFieldType.year(), iDefaultYear);
            }
        }

        for (int i = 0; i < count; i++) {
            millis = savedFields[i].set(millis, resetFields);
        }
    } catch (IllegalFieldValueException e) {
        if (text != null) {
            e.prependMessage("Cannot parse \"" + text + '"');
        }
        throw e;
    }

    // Adjust for time zone effects, handling local time and standard time differently
    if (iZone == null) {
        millis -= iOffset;
    } else {
        int offset = iZone.getOffsetFromLocal(millis);
        millis -= offset;
        // Check offsets to handle special transitions like daylight saving time changes
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
