public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    ZonedDateTime zdt = chrono.toDateTime(instantMillis);
    chrono = selectChronology(chrono);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withChronology(chrono);
    LocalDate localDate;

    try {
        localDate = LocalDate.parse(text, formatter.withZone(chrono.getZone()));
    } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid date format", e);
    }

    if (localDate.getMonthValue() == 2 && localDate.getDayOfMonth() == 29 && !isLeapYear(localDate.getYear())) {
        throw new IllegalArgumentException("The provided date is not valid: February 29 in a non-leap year");
    }

    ZonedDateTime parsedZonedDateTime = zdt.with(localDate);
    instant.setMillis(parsedZonedDateTime.toInstant().toEpochMilli());
    
    if (iOffsetParsed && bucket.getOffsetInteger() != null) {
        int parsedOffset = bucket.getOffsetInteger();
        DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
        chrono = chrono.withZone(parsedZone);
    } else if (bucket.getZone() != null) {
        chrono = chrono.withZone(bucket.getZone());
    }
    instant.setChronology(chrono);

    return position + 1; // Assuming the parser advances the position by one
}

private boolean isLeapYear(int year) {
    return ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
}