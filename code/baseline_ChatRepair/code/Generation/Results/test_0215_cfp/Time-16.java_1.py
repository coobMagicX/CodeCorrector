public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);
    
    // Determine the effective default year to be used
    int defaultYear = (iDefaultYear != null) ? iDefaultYear : instant.getChronology().year().get(instantMillis);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    long newMillis = bucket.computeMillis(false, text);

    // Adjust chronology with the parsed time zone or offset
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }

    // Set the new chronology and time zone before updating millis
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    instant.setMillis(newMillis);

    return newPos;
}
