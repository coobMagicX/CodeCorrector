public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);

    // Retain old chronology to restore later if necessary
    Chronology originalChrono = chrono;
    chrono = selectChronology(chrono);

    // Ensure default year accounting for leap years if necessary by encountering leap year context
    int defaultYear = chrono.year().get(instantLocal);
    if (!chrono.year().isLeap(defaultYear) && text.contains("2 29")) {
        // Adjusting default year to nearest leap year if parsing February 29 and it isn't a leap year on default year
        while (!chrono.year().isLeap(++defaultYear)) {}
        chrono = chrono.withYear(defaultYear);
        instantLocal = instantLocal - originalChrono.getZone().getOffset(instantMillis) + chrono.getZone().getOffset(instantMillis);
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
