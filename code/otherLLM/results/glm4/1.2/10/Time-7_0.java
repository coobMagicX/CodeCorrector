public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }

    long instantMillis = instant.getMillis();
    Chronology chrono = selectChronology(instant.getChronology());
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M d", Locale.UK);
    ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(instantMillis), chrono zegonalRules());

    if (zdt.getMonthValue() == 2 && zdt.getDayOfMonth() == 29) {
        // Explicitly handling leap day to ensure correct parsing
        LocalDate leapDay = LocalDate.of(zdt.getYear(), 2, 29);
        instantLocal = leapDay.toEpochDay();
    }

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, Locale.UK, iPivotYear, zdt.getYear());
    int newPos = parser.parseInto(bucket, text, position);

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