public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = selectChronology(instant.getChronology());
    long offset = chrono.getZone().getOffset(instantMillis);
    long instantLocal = instantMillis + offset;

    // Use the local timestamp to get contextual information such as the year 
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono.withUTC(), iLocale, iPivotYear, chrono.year().get(instantLocal));

    int newPos = parser.parseInto(bucket, text, position);
    
    if (newPos >= 0) {
        instant.setMillis(bucket.computeMillis(true)); // 'true' to reset time part
    }

    // After parsing, obtain and adjust final zone and chronology settings
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(bucket.getOffsetInteger());
        instant.setZone(parsedZone);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        instant.setZone(bucket.getZone());
        chrono = chrono.withZone(bucket.getZone());
    } else if (iZone != null) {
        instant.setZone(iZone);
    }

    instant.setChronology(chrono);

    return newPos;
}
