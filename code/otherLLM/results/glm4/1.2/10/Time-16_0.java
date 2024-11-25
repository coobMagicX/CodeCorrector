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
    int newPos = parser.parseInto(bucket, text, position);

    // Ensure the parsing function handles month-only input correctly
    if (text.matches("\\d+M")) { // Check if the input text is in the form of "MM"
        int month = Integer.parseInt(text.replace("M", ""));
        if (month > 12 || month < 1) {
            throw new IllegalArgumentException("Invalid month value: " + month);
        }
        bucket.setMonth(month - 1); // Set the month value, adjusting for zero-based index
    } else {
        instant.setMillis(bucket.computeMillis(false, text));
    }

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

    // Reset the day to 1 for month-only parsing
    if (text.matches("\\d+M")) {
        bucket.setDayOfMonth(1); // Set the day to 1, which is necessary when only month is provided
    }
    
    return newPos;
}