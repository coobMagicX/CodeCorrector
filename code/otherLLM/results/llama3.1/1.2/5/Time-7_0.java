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
    
    // Check if the input string represents a month and day (e.g., "M d") format
    if (text.startsWith("0")) {
        // If it does, parse it as such
        DateTimeParserBucket bucket = new DateTimeParserBucket(
            instantLocal, chrono, iLocale, iPivotYear, defaultYear);
        int newPos = parser.parseInto(bucket, text, position);
        instant.setMillis(bucket.computeMillis(false, text));
        
        // Since the input string represents a month and day, we need to calculate the year manually
        Calendar calendar = new GregorianCalendar(chrono.getZone());
        calendar.setTimeInMillis(instantLocal);
        int year = calendar.get(Calendar.YEAR);
        chrono = chrono.withYear(year);
        instant.setChronology(chrono);
    } else {
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
    }
    
    return newPos;
}