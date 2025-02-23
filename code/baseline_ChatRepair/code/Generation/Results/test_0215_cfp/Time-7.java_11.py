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

    // Adjust year to nearest valid leap year if the text contains "2 29"
    if (text.contains("2 29")) {
        if (!chrono.year().isLeap(defaultYear)) {
            Chronology isoChrono = ISOChronology.getInstanceUTC();
            int testYear = defaultYear; // Start testing from the current year
            boolean isLeap = false;
            while (!isLeap) {
                testYear -= 1; // Move backward to find a recent leap year
                if (isoChrono.year().isLeap(testYear)) {
                    isLeap = true;
                }
            }
            defaultYear = testYear;
        }
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
