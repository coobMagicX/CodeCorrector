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
    
    DateTimeFormatter formatter = DateTimeFormatters.localDefault(); // Use local formatter for leap year check
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);

    if (bucket.getDayOfMonth() == 29 && bucket.getMonthOfYear() == 2) { // Check for February 29
        boolean isLeapYear = formatter.getChronology().isLeapYear(bucket.getYear());
        if (!isLeapYear) { // If not a leap year, correct the day to February 28
            int lastDayOfMonth = formatter.getChronology().dayOfMonth().getMaximum(new DateTime(bucket.getYear(), 2, 1, 0, 0, 0, chrono));
            bucket.setDayOfMonth(lastDayOfMonth);
        }
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