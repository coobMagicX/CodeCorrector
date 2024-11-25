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

    // Correcting the parsing logic for single digit month values
    if (text.length() == 1 && "M".equals(text)) {
        // Increment year and set month to January
        bucket.setMillis(bucket.getMillis() + ((bucket.getChronology().getYearMillis(bucket.getMillis()) + 1000) - bucket.getChronology().getYearMillis(bucket.getMillis())));
        bucket.setField(DateTimeField.MONTH_OF_YEAR, 1);
    } else {
        // Use the parser to parse as normal
        newPos = parser.parseInto(bucket, text, position);
    }

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

private DateTimeParser requireParser() {
    DateTimeParser parser = iParser;
    if (parser == null) {
        throw new UnsupportedOperationException("Parsing not supported");
    }
    return parser;
}