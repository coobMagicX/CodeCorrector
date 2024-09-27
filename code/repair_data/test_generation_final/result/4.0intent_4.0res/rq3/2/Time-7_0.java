public int parseInto(ReadWritableInstant instant, String text, int position) {
    // Ensure that a parser is available
    DateTimeParser parser = requireParser();
    
    // Check if the instant is null, if so throw an exception
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    // Get the millisecond time from the instant
    long instantMillis = instant.getMillis();
    // Retrieve the chronology from the instant
    Chronology chrono = instant.getChronology();
    // Adjust instant millis based on the timezone offset
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    // Select the appropriate chronology based on the input
    chrono = selectChronology(chrono);
    // Get the default year from the chronology for current local millis
    int defaultYear = chrono.year().get(instantLocal);
    
    // Create a new DateTimeParserBucket with the adjusted parameters
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    // Parse the text starting at the given position
    int newPos = parser.parseInto(bucket, text, position);
    // Set the millis of the instant as computed from the bucket
    instant.setMillis(bucket.computeMillis(false, text));
    // Check if an offset was parsed and adjust the chronology accordingly
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    // Set the chronology of the instant
    instant.setChronology(chrono);
    // If a specific zone is provided, set it to the instant
    if (iZone != null) {
        instant.setZone(iZone);
    }
    // Return the new position after parsing
    return newPos;
}