public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        savedFields = (SavedField[])iSavedFields.clone();
        iSavedFields = savedFields;
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);

    if (count > 0) {
        // alter base year for parsing if an adjustment is needed
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        DurationField weeks = DurationFieldType.weeks().getField(iChrono);
        DurationField first = savedFields[0].iField.getDurationField();
        if (compareReverse(first, months) >= 0 && compareReverse(first, weeks) <= 0) {
            // Change base year only if first field is not a week-related field, because week-based parsing uses weekyear
            if (!(savedFields[0].iField.getType() == DateTimeFieldType.weekyear() || savedFields[0].iField.getType() == DateTimeFieldType.weekOfWeekyear())) {
                saveField(DateTimeFieldType.year(), iDefaultYear);
            }
            return computeMillis(resetFields, text);
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
