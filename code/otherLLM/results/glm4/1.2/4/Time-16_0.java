public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    // Ensure that the year component remains unchanged by not altering it
    long startYear = instant.getChronology().getYear(instantMillis);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantMillis, chrono, iLocale, iPivotYear, iDefaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    
    // Set the year back to the original value after parsing
    bucket.setChronology(chrono.withYear(startYear));
    
    instant.setMillis(bucket.computeMillis(false, text));
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    
    // Only update the month part of the instant, not other date components
    int monthParsed = bucket.getMonth();
    if (monthParsed != -1) { // -1 is the default value indicating no parsing occurred for this component
        long newMillis = chrono.setMonth(instantMillis, monthParsed);
        instant.setMillis(newMillis);
    }
    
    instant.setChronology(chrono);
    if (iZone != null) {
        instant.setZone(iZone);
    }
    return newPos;
}