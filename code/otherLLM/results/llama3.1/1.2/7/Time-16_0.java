public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = selectChronology(instant.getChronology());
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, iDefaultYear);
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

private Chronology selectChronology(Chronology chrono) {
    chrono = DateTimeUtils.getChronology(chrono);
    if (iChrono != null) {
        chrono = iChrono;
    }
    if (iZone != null) {
        chrono = chrono.withZone(iZone);
    }
    return chrono;
}