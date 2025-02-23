public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;

    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);

    if (count > 0) {
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        DurationField first = savedFields[0].iField.getDurationField();
        // Adjust computation strategy based on the type of the first field
        if (compareReverse(first, months) >= 0 && compareReverse(first, days) <= 0) {
            if (DateTimeFieldType.year().equals(savedFields[0].iField.getType())) {
               // Ensure the year is saved with the context of a week year if needed
                int yearOfEraIndex = -1;
                for (int i = 0; i < count; i++) {
                    if (DateTimeFieldType.weekyear().equals(savedFields[i].iField.getType())) {
                        yearOfEraIndex = i;
                        break;
                    }
                }
                if (yearOfEraIndex >= 0) {
                    // If there's a weekyear, save the current year field and recompute
                    saveField(DateTimeFieldType.year(), iDefaultYear);
                }
            }
        }
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
