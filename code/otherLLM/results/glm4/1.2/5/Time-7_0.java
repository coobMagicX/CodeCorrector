public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeFormatter formatter = withDefaultYear(instant.getMillis());
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);
    int defaultYear = formatter.getYear();

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, formatter.getLocale(), iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    long computedMillis = bucket.computeMillis(false, text);
    if (computedMillis != instantMillis) {
        Chronology newChrono = selectChronology(chrono.withInstant(instant.toDateTime().withMillis(computedMillis)));
        instant.setChronology(newChrono);
    }

    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}