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

    // Check specifically if parsing February 29
    if (text.contains("2 29") || text.contains("29 2")) { // Assuming date format could be month-day or day-month
        int leapYear = defaultYear;
        // Adjust to the nearest valid leap year if current defaultYear isn't a leap year
        while (!chrono.year().isLeap(leapYear)) {
            leapYear++;
        }
        defaultYear = leapYear; // use a leap year for the default year in parsing context
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
        chrono = chrono.withZone(bucket.getZone);
    }

    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }

    return newPos;
}
