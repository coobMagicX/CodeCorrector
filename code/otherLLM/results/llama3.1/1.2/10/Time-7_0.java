public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);
    int defaultYear = chrono.year().get(instantLocal);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    instant.setMillis(bucket.computeMillis(false, text));
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chronologyWithZone(chrono, parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chronologyWithZone(chrono, bucket.getZone());
    }
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}

private Chronology chronologyWithZone(Chronology chrono, DateTimeZone zone) {
    if (zone == null) {
        return chrono;
    } else {
        return chrono.withZone(zone);
    }
}