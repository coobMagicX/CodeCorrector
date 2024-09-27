public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    long instantLocal = instantMillis + chrono.getZone().getOffset(instantMillis);
    chrono = selectChronology(chrono);
    
    // Save original values to preserve them later
    DateTimeField yearField = chrono.year();
    DateTimeField monthField = chrono.monthOfYear();
    DateTimeField dayField = chrono.dayOfMonth();
    DateTimeField hourField = chrono.hourOfDay();
    DateTimeField minuteField = chrono.minuteOfHour();
    DateTimeField secondField = chrono.secondOfMinute();
    DateTimeField millisField = chrono.millisOfSecond();
    
    // Save original date and time values
    int originalYear = yearField.get(instantMillis);
    int originalMonth = monthField.get(instantMillis);
    int originalDay = dayField.get(instantMillis);
    int originalHour = hourField.get(instantMillis);
    int originalMinute = minuteField.get(instantMillis);
    int originalSecond = secondField.get(instantMillis);
    int originalMillis = millisField.get(instantMillis);

    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantLocal, chrono, iLocale, iPivotYear, iDefaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    
    // Compute new millis based on parsed data and preserve other fields
    long newMillis = bucket.computeMillis(false, text);
    
    // Resetting the fields that should not change if not specified
    if (newPos > 0) {
        yearField.set(newMillis, originalYear);
        monthField.set(newMillis, originalMonth);
        dayField.set(newMillis, originalDay);
        hourField.set(newMillis, originalHour);
        minuteField.set(newMillis, originalMinute);
        secondField.set(newMillis, originalSecond);
        millisField.set(newMillis, originalMillis);
    }

    instant.setMillis(newMillis);
    
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