public int parseInto(ReadWritableInstant instant, String text, int position) {
    // Ensure we have a parser to work with
    DateTimeParser parser = requireParser();
    
    // Throw an exception if the instant is null
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    // Get the current millisecond value of the instant
    long instantMillis = instant.getMillis();
    
    // Retrieve the chronology of the instant
    Chronology chrono = instant.getChronology();
    
    // Calculate local milliseconds considering the time zone offset
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    
    // Select the appropriate chronology based on the current context
    chrono = selectChronology(chrono);
    
    // Get the default year from the chronology for the local time
    int defaultYear = chrono.year().get(instantLocal);
    
    // Create a new DateTimeParserBucket for parsing
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    
    // Parse the text starting from the given position
    int newPos = parser.parseInto(bucket, text, position);
    
    // Set the new millisecond value to the instant
    instant.setMillis(bucket.computeMillis(false, text));
    
    // Adjust the chronology based on the parsed offset or zone if available
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    
    // Update the chronology of the instant
    instant.setChronology(chrono);
    
    // Set the zone from the outer context if it is specified
    if (iZone != null) {
        instant.setZone(iZone);
    }
    
    // Return the new position after parsing
    return newPos;
}