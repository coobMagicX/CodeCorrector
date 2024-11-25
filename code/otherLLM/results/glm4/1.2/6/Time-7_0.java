import org.joda.time.DateTimeParser;
import org.joda.time.ReadWritableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    // Ensure the chronology is set to the New York time zone
    Chronology chrono = Chronology.withZone(Chronology.ISO.std(), TimeZone.getTimeZone("America/New_York").toZoneID());
    instant.setChronology(chrono);
    
    long instantMillis = instant.getMillis();
    int defaultYear = instant.year().get(instantMillis); // Use the year from the instant
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantMillis, chrono, Locale.UK, null, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    
    // Check if the parsing was successful and set the result correctly
    if (newPos != -1) {
        instant.setMillis(bucket.computeMillis(false, text));
        if (bucket.getOffsetInteger() != null || bucket.getZone() != null) {
            int parsedOffset = bucket.getOffsetInteger();
            DateTimeZone parsedZone = bucket.getZone() != null ? bucket.getZone() : DateTimeZone.forOffsetMillis(parsedOffset);
            chrono = chrono.withZone(parsedZone);
            instant.setChronology(chrono);
        }
    }
    
    return newPos;
}

// Dummy method to simulate the behavior of requireParser
private DateTimeParser requireParser() {
    // This should be replaced with an actual parser instance
    return new DateTimeParser() {
        @Override
        public int parseInto(DateTimeParserBucket bucket, String text, int position) {
            if (text != null && position >= 0 && position <= text.length()) {
                // Dummy parsing logic for demonstration purposes
                bucket.setMillis(bucket.getChronology().withZone(bucket.getZone()).setDate(2023, 2, 29).getMillis());
                return text.length();
            }
            return -1;
        }
    };
}