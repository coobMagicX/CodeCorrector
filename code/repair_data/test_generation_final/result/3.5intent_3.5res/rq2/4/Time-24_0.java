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
        if (text != null) {
            String[] dateFields = text.split("-");
            int year = Integer.parseInt(dateFields[0]);
            int month = Integer.parseInt(dateFields[1]);
            int week = Integer.parseInt(dateFields[2]);
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern("xxxx-MM-ww")
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .parseDefaulting(ChronoField.MILLISECOND_OF_SECOND, 0)
                    .toFormatter();
            LocalDate date = LocalDate.parse(text, formatter);
            millis -= iZone.getOffsetFromLocal(date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
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
    }
    
    return millis;
}