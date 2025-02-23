public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    chrono = selectChronology(chrono);
    long zoneOffset = chrono.getZone().getOffset(instantMillis);
    long instantLocal = instantMillis + zoneOffset;

    // Estimate a default year from the current instant to aid in parsing month and day correctly
    int defaultYear = chrono.year().get(instantLocal);

    // Use the default year to initialize the parsing bucket
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    
    // Parse the text, position could be adjusted as necessary by specific inputs
    int newPos = parser.parseInto(bucket, text, position);
    
    // Ensure any field changes including year, month, day are resolved
    long newMillis = bucket.computeMillis(false, text);
    
    // Update the instant's millis and chronology after all adjustments are finalized
    instant.setMillis(newMillis);
    
    // If a specific offset was parsed, apply it
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    
    // Update the chronology last to ensure it encompasses any time zone modifications
    instant.setChronology(chrono);
    
    if (iZone != null) {
        instant.setZone(iZone);
    }
    
    return newPos;
}
