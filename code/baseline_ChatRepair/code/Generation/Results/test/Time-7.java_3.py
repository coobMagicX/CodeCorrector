public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = selectChronology(instant.getChronology());
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    
    // First use a temporary bucket to parse fields without modifying the original instant
    int defaultYear = chrono.year().get(instantLocal);
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    
    // Compute millis from the parsed bucket
    long computedMillis = bucket.computeMillis(false, text);
    
    // Adjust chronology and timezone based on parsed data
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    
    instant.setChronology(chrono);
    instant.setMillis(computedMillis);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    
    return newPos;
}
