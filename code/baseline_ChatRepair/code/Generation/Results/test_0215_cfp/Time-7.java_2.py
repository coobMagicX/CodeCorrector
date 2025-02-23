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

    // Creating a sufficiently mutable datetime to find an appropriate chronology if leap year parsing is needed
    DateTime dt = new DateTime(instantMillis, chrono);
    if (!chrono.year().isLeap(defaultYear) && text.contains("2 29")) {
        do {
            dt = dt.plusYears(1); // efficiently finds next leap year
        } while (!dt.year().isLeap());
        chrono = dt.getChronology(); // update chronology to have the correct year setup
        instantLocal = dt.getMillis();  // also update the local instant time to reflect this
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
