public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, iDefaultYear);

    // Use the parser to parse text from the current position onwards
    int newPos = parser.parseInto(bucket, text, position);

    // Ensure that the month parsing is done correctly by checking for single digits and setting accordingly.
    if (newPos > 0 && bucket.getMonth() != null) {
        int monthValue = bucket.getMonth();
        if (monthValue < 10) { // Single digit month
            monthValue += 1; // Assume January is represented by 1, etc.
        }
        instant.setMonth(monthValue - 1); // Java months are 0-indexed
    } else {
        // If the text does not contain a valid month, set to the current month of the instant
        instant.setMonth(instant.getMonth());
    }

    // Proceed with setting millis and chronology as before
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