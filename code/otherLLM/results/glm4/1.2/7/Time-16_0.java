public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono); // Keep chronology as it is
    
    DateTimeFormatter formatter = new DateTimeFormatter()
            .withChronology(chrono) // Apply the selected chronology to the formatter
            .parseLenient(text, position);

    if (formatter.isValid()) {
        long parsedMillis = formatter.getMillis();
        instant.setMillis(parsedMillis);
        
        // Reconstruct the ReadWritableInstant with the new millis and original chronology
        ChronoField[] fields = {ChronoField.YEAR, ChronoField.MONTH_OF_YEAR, ChronoField.DAY_OF_MONTH,
                                 ChronoField.HOUR_OF_DAY, ChronoField.MINUTE_OF_HOUR, ChronoField.SECOND_OF_MINUTE};
        
        DateTimeParsePosition pos = new DateTimeParsePosition(0);
        for (ChronoField field : fields) {
            if (!formatter.isValid(pos)) break;
            long value = formatter.getPartialValue(pos, field);
            if (field == ChronoField.MONTH_OF_YEAR && value < 1 || value > 12) {
                throw new IllegalArgumentException("Invalid month value: " + value);
            }
        }

        // Ensure other fields remain unchanged
        chronology.withFieldsResolved(true)
                  .withChronology(chrono)
                  .set(instantMillis, ChronoField.HOUR_OF_DAY, formatter.getHourOfDay(pos),
                       ChronoField.MINUTE_OF_HOUR, formatter.getMinuteOfHour(pos),
                       ChronoField.SECOND_OF_MINUTE, formatter.getSecondOfMinute(pos),
                       null, null);
    }

    if (iOffsetParsed && formatter.getOffset() != null) {
        int parsedOffset = formatter.getOffset();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (formatter.getZone() != null) {
        chrono = chrono.withZone(formatter.getZone());
    }

    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return position + formatter.getValue();
}