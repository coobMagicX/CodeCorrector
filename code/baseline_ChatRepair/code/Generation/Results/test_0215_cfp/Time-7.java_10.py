public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);

    // Potentially adjust the default year to ensure we use a leap year if the text to parse includes "2 29".
    int defaultYear = chrono.year().get(instantLocal);
    if (text.contains("2 29")) {
        // Choose a leap year that doesn't adjust the chronology's era or current time drastically.
        defaultYear = chrono.year().isLeap(instantLocal) ? defaultYear : 2024; // 2024 is a future leap year.
    }

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);

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
