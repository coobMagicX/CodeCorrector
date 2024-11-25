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
        
        if (first instanceof DateField) {
            // Check if first field is valid date
            if (!((DateField)first).isValid()) {
                throw new IllegalFieldValueException("Invalid date");
            }
            
            // If the first field is a month or day, check if it's within valid range for year
            int maxYear = ((DateField)first).getMaxValue();
            int minYear = ((DateField)first).getMinValue();
            long millis = iMillis;
            
            try {
                millis = savedFields[0].set(millis, resetFields);
            } catch (IllegalFieldValueException e) {
                if (text != null) {
                    e.prependMessage("Cannot parse \"" + text + '"');
                }
                throw e;
            }
            
            long year = millis / 31536000000L; // Convert millis to years
            
            if (year < minYear || year > maxYear) {
                String message =
                    "Illegal instant due to date being out of range for year";
                if (text != null) {
                    message = "Cannot parse \"" + text + "\": " + message;
                }
                throw new IllegalArgumentException(message);
            }
        } else if (compareReverse(first, months) >= 0 && compareReverse(first, days) <= 0) {
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