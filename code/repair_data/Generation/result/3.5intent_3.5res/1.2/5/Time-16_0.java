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
    
    long parsedMillis = bucket.computeMillis(false, text);
    MutableDateTime mutableInstant = instant.toMutableDateTime();
    mutableInstant.setMillis(parsedMillis);
    mutableInstant.setMonthOfYear(bucket.getDateTime().getMonthOfYear());
    
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    instant.setMillis(mutableInstant.getMillis());
    instant.setChronology(chrono);
    
    if (iZone != null) {
        instant.setZone(iZone);
    }
    
    return newPos;
}