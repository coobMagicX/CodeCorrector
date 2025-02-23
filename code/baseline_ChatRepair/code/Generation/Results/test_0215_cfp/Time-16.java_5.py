public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();

    // Convert to local time before parsing
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);

    // Update to the appropriate chronology for parsing
    chrono = selectChronology(chrono);

    // Extract the year from the instant's current value
    int defaultYear = chrono.year().get(instantMillis);

    // Set up the parsing bucket with the year from the instant considered as the default year
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);

    int newPos = parser.parseInto(bucket, text, position);

    // Compute the instant millis after parsing
    instant.setMillis(bucket.computeMillis(false, text));

    // Process parsed zone information or offsets
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }

    // Set the updated chronology
    instant.setChronology(chrono);

    // Apply any externally specified zone
    if (iZone != null) {
        instant.setZone(iZone);
    }

    return newPos;
}
