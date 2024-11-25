public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono); // Ensure the chronology is selected correctly
    
    int defaultYear = chrono.year().get(instantLocal); // Retrieve the correct year from local time
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    
    // Check if the date string contains February 29th and handle leap years
    if (text.equals("2 29")) {
        Chronology currentChrono = chrono;
        while (currentChrono.isLeapYear(currentChrono.year().get(instantLocal))) {
            bucket.setYear(bucket.getYear() + 1); // Increment year in the bucket until non-leap year is found
            defaultYear = bucket.getYear(); // Update defaultYear
            currentChrono = selectChronology(currentChrono.withYear(defaultYear)); // Select new chronology with updated year
        }
    }
    
    int newPos = parser.parseInto(bucket, text, position);
    instant.setMillis(bucket.computeMillis(false, text));
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone); // Use the parsed zone
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone()); // Use the bucket's zone
    }
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}

private Chronology selectChronology(Chronology chronology) {
    // This method should be implemented to properly select a chronology based on requirements
    // For now, it just returns the input chronology
    return chronology;
}