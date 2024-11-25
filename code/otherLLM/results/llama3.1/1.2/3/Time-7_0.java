public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null || text == null) {
        throw new IllegalArgumentException("Instant and text must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = selectChronology(instant.getChronology());
    int defaultYear = chrono.year().get(instantMillis);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantMillis, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    instant.setMillis(bucket.computeMillis(false, text));
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = selectChronology(chrono.withZone(parsedZone));
    } else if (bucket.getZone() != null) {
        chrono = selectChronology(chrono.withZone(bucket.getZone()));
    }
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}