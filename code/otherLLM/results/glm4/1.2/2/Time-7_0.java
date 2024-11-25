private Chronology selectChronology(Chronology chrono) {
    chrono = DateTimeUtils.getChronology(chrono);
    if (iChrono != null) {
        chrono = iChrono;
    }
    if (iZone != null) {
        chrono = chrono.withZone(iZone);
    }
    return chrono;
}

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

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    // Use the date format "M d" to ensure month is parsed as month
    String dateFormat = "MM dd";
    int newPos = parser.parseInto(bucket, text, position, dateFormat);
    instant.setMillis(bucket.computeMillis(false, dateFormat));
    
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

    // Ensure the month parsed is set correctly
    int monthIndex = bucket.getMonth();
    if (monthIndex != -1) {
        instant.setMonth(monthIndex);
    }
    
    return newPos;
}