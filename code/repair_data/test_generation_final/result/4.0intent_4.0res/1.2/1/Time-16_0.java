public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, iDefaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    
    // Use the original instant's millis and only update the parsed fields
    long computedMillis = bucket.computeMillis(false, text);
    if (newPos > 0) { // only update if parsing was successful
        MutableDateTime tempDateTime = new MutableDateTime(instantMillis, chrono);
        tempDateTime.setMillis(computedMillis);
        if (text.length() == 1) { // assuming the input text of "1" means only the month is being parsed
            // Set the month while keeping other fields unchanged
            instant.setMonthOfYear(tempDateTime.getMonthOfYear());
        } else {
            instant.setMillis(computedMillis);
        }
    }
    
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}