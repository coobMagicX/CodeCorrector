import org.joda.time.ReadWritableInstant;
import org.joda.time.DateTimeParserBucket;
import org.joda.time.chrono.Chronology;
import org.joda.time.DateTimeZone;

public int parseInto(ReadWritableInstant instant, String text, int position) {
    DateTimeParser parser = requireParser();
    if (instant == null) {
        throw new IllegalArgumentException("Instant must not be null");
    }
    
    long instantMillis = instant.getMillis();
    Chronology chrono = instant.getChronology();
    // Adjust chronology to the NY time zone before parsing
    chrono = Chrono.newInstance(chrono, DateTimeZone.forID("America/New_York"));
    int defaultYear = chrono.year().get(instantMillis);
    
    DateTimeParserBucket bucket = new DateTimeParserBucket(
        instantMillis, chrono, iLocale, iPivotYear, defaultYear);
    int newPos = parser.parseInto(bucket, text, position);
    instant.setMillis(bucket.computeMillis(false, text));
    
    // Ensure the parsed date is in the NY time zone
    if (bucket.getZone() != null) {
        DateTimeZone bucketZone = bucket.getZone();
        chrono = Chrono.newInstance(chrono, bucketZone);
    }
    
    instant.setChronology(chrono);
    if (iZone != null && !bucket.getZone().equals(iZone)) {
        // If the zone from parsing does not match iZone, set it
        instant.setZone(iZone);
    }

    return newPos;
}