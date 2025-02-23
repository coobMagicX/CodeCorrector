public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);

    // Use a leap year as the default year to ensure "2 29" can be parsed initially.
    int defaultYear = chrono.year().isLeap(instantLocal) ? chrono.year().get(instantLocal) : 2000; // any fixed leap year, e.g., 2000

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);

    // Correct the chronology based on the actual year parsed which might not be a leap year.
    if (bucket.getParsedFields().contains(DateTimeFieldType.dayOfMonth())
        && bucket.getParsedFields().contains(DateTimeFieldType.monthOfYear())
        && !chrono.year().isLeap(bucket.getYear())) {
        chrono = ISOChronology.getInstanceUTC();
        if (bucket.getYear() % 4 != 0 || (bucket.getYear() % 100 == 0 && bucket.getYear() % 400 !=0 )) {
            throw new IllegalArgumentException("Parsed year is not a leap year but Feb 29 was parsed.");
        }
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
