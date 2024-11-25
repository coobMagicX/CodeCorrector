public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeFormatter formatter = requireParser().withChronology(instant.getChronology()).withLocale(iLocale).withPivotYear(iPivotYear).withDefaultYear(iDefaultYear);

    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, formatter, iPivotYear, iDefaultYear);

    int newPos = formatter.parseInto(bucket, text, position);
    if (newPos == -1) {
        // Handle the case where parsing failed
        return -1;
    }
    
    instant.setMillis(bucket.computeMillis(false, text));
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