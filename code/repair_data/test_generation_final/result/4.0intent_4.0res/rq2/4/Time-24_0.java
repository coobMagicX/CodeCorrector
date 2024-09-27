public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);
    if (count > 0) {
        // Check if parsing in the format "weekyear-month-week"
        DurationField weeks = DurationFieldType.weeks().getField(iChrono);
        DurationField first = savedFields[0].iField.getDurationField();
        boolean isWeekFormat = first.equals(weeks);

        // If parsing week format, adjust fields appropriately
        if (isWeekFormat) {
            DateTimeField weekyearField = DateTimeFieldType.weekyear();
            DateTimeField weekOfWeekyearField = DateTimeFieldType.weekOfWeekyear();

            // Set default values for weekyear and week of weekyear fields
            saveField(weekyearField, iDefaultYear);
            saveField(weekOfWeekyearField, 1);

            // Re-run computation to ensure proper weekyear setup
            return computeMillis(resetFields, text);
        }

        // Alter base year for parsing if first field is month or day
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        if (compareReverse(first, months) >= 0 && compareReverse(first, days) <= 0) {
            saveField(DateTimeFieldType.year(), iDefaultYear);
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