public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();

    
    chrono = selectChronology(chrono);
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);

    
    int defaultYear = chrono.year().get(instantMillis);

    
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);

    
    int newPos = parser.parseInto(bucket, text, position);

    
    
    instant.setMillis(bucket.computeMillis(false, text));

    
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone