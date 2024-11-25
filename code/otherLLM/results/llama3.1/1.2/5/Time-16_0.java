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
    instant.setMillis(bucket.computeMillis(false, text));
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone).withMillis(instantLocal + parsedOffset);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone()).withMillis(instantLocal + bucket.getZone().getOffset(instantLocal));
    }
    instant.setChronology(chrono);
    instant.setMillis(chrono.withZone(iZone).getMillis());
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}