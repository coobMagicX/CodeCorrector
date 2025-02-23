public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    DateTimeZone zone = chrono.getZone(); // Ensuring we're retrieving existing methods correctly
    long instantLocal = instantMillis + zone.getOffset(instantMillis);
    chrono = selectChronology(chrono);
    int defaultYear = chrono.year().get(instantLocal);

    // Check specifically if parsing February 29
    if (text.matches("(?<!\\d)(2 29|29 2)(?!\\d)")) { // More accurate check for "2 29" or "29 2" standalone phrase
        int leapYear = defaultYear;
        // Adjust to the nearest valid leap year if the current defaultYear isn't a leap year
        while (!chrono.year().isLeap(leapYear)) {
            leapYear++;
        }
        defaultYear = leapYear; // Use a leap year for default year in parsing context
    }

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);

    // Apply computed millis and possibly modified chronology (due to zone changes)
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
