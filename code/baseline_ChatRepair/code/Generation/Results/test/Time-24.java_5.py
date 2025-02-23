public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;

    if (iSavedFieldsShared) {
        savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFields = savedFields;
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);

    if (count > 0) {
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        DurationField firstDurationField = savedFields[0].iField.getDurationField();

        // Trigger year setting when first field is weekyear
        if (savedFields[0].iField.getType() == DateTimeFieldType.weekyear()) {
            saveField(DateTimeFieldType.year(), iDefaultYear);
            savedFields[0].iField = DateTimeFieldType.year().getField(iChrono); // Change weekyear to year parsing context
        }
        else if (compareReverse(firstDurationField, months) >= 0 && compareReverse(firstDurationField, days) <= 0) {
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
            e.prependMessage("Cannot parse \"" + text + '"');
        }
        throw e;
    }

    if (iZone != null) {
        int offset = iZone.getOffsetFromLocal(millis);
        millis -= offset;
        if (offset != iZone.getOffset(millis)) {
            String msg = "Illegal instant due to time zone offset transition (" + iZone + ')';
            if (text != null) {
                msg = "Cannot parse \"" + text + "\": " + msg;
            }
            throw new IllegalArgumentException(msg);
        }
    } else {
        millis -= iOffset;
    }

    return millis;
}
