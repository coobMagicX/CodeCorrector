public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);
    
    // Determine the effective default year to be used based on whether iDefaultYear is null or not
    int effectiveDefaultYear = iDefaultYear != null ? iDefaultYear : chrono.year().get(instantMillis);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, effectiveDefaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    long newMillis = bucket.computeMillis(false, text);

    // Adjust the chronology manager based on parsed information
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone);
    }

    // Apply the new zone and chronology
    if (iZone != null) {
        instant.setZone(iZone);
    }
    instant.setChronology(chrono);
    instant.setMillis(newMillis);

    return newPos;
}
