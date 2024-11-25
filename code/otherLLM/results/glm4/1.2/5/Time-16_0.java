public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    // Ensure the chronology is consistent with the chronology of the instant before parsing
    chrono = selectChronology(chrono);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantMillis, chrono, iLocale, iPivotYear, iDefaultYear); // Use the original instantMillis for bucket initialization

    int newPos = parser.parseInto(bucket, text, position);

    // After parsing, ensure we only set the millis if necessary and avoid changing chronology or zone
    if (bucket.getOffsetInteger() != null && iOffsetParsed) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone); // Update chronology with new Zone if offset is parsed
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone()); // Set the zone for the chronology if provided
    }

    instant.setChronology(chrono);
    if (iZone != null && !Instant.getUTCZone().equals(iZone)) { // Only set zone if a non-UTC zone is specified
        instant.setZone(iZone);
    }
    
    return newPos;
}