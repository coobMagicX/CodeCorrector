public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    // Retrieve the current millis and chronology from the instant
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();

    // Adjust for the time zone offset to deal with local time
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);

    // Update the chronology based on the parser settings
    chrono = selectChronology(chrono);

    // Use the year of the current instant as the default year for parsing
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, instant.getYear());
    int newPos = parser.parseInto(bucket, text, position);
    
    // Compute and set the new millis based on parsing, without adjusting for zone
    instant.setMillis(bucket.computeMillis(false, text));

    // Check if a new offset has been parsed
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    }
    // Check if a new zone has been parsed
    else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }

    // Set the updated chronology on the instant
    instant.setChronology(chrono);

    // Apply the external zone if specified
    if (iZone != null) {
        instant.setZone(iZone);
    }
    
    return newPos;
}
