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
    
    // Compute new millis from the bucket and apply changes
    long computedMillis = bucket.computeMillis(false, text);
    
    // Update chronology if a new zone or offset was parsed
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }

    // Set both the new chronology and millis
    instant.setChronology(chrono); 
    instant.setMillis(computedMillis); // Ensure setting millis after setting chronology

    // Update the zone of instant object if necessary
    if (iZone != null) {
        instant.setZone(iZone);
    }

    return newPos;
}
