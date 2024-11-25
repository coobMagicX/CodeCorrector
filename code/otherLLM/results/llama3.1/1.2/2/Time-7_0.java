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
    
    // Validate month and day parsing
    if (bucket.getMonth() < 1 || bucket.getMonth() > 12) {
        throw new IllegalArgumentException("Invalid month: " + bucket.getMonth());
    }
    if (bucket.getDayOfMonth() < 1 || bucket.getDayOfMonth() > 31) {
        throw new IllegalArgumentException("Invalid day of month: " + bucket.getDayOfMonth());
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
    
    // Verify zone offset calculation
    long expectedLocalMillis = instantMillis + parsedZone.getOffset(instantMillis);
    if (instantLocal != expectedLocalMillis) {
        throw new IllegalArgumentException("Incorrect zone offset: " + instantLocal + ", expected: " + expectedLocalMillis);
    }
    
    return newPos;
}