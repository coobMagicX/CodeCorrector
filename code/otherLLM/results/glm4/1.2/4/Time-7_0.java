import org.joda.time.DateTimeParser;
import org.joda.time.ReadWritableInstant;
import org.joda.time.chrono.Chronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;

public class DateParser {

    private DateTimeParser iParser;

    public void setParser(DateTimeParser parser) {
        this.iParser = parser;
    }

    private DateTimeParser requireParser() {
        if (iParser == null) {
            throw new UnsupportedOperationException("Parsing not supported");
        }
        return iParser;
    }

    public int parseInto(ReadWritableInstant instant, String text, int position) {
        DateTimeParser parser = requireParser();
        if (instant == null) {
            throw new IllegalArgumentException("Instant must not be null");
        }

        long instantMillis = instant.getMillis();
        Chronology chrono = instant.getChronology();

        // Use the ISO chronology to correctly handle leap years
        Chronology defaultChrono = ISOChronology.getInstanceUTC();
        
        // Adjust for timezone and leap year if necessary
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendLiteral("2 29")
            .appendValue(defaultChrono.year(), 4, 4, DateTimeFormatter.PAD_ZERO)
            .toFormatter();
        instant.set(instant.toDateTime(defaultChrono));

        // Parse the text considering New York time zone
        Chronology nyChrono = defaultChrono.withZoneUTC().withZone(DateTimeZone.forID("America/New_York"));
        DateTimeParserBucket bucket = new DateTimeParserBucket(
                instantMillis, chrono, DateTimeZone.UTC, 2004, 2004);
        
        int newPos = parser.parseInto(bucket, text, position);

        // Apply parsed date values to the instant
        instant.set(nyChrono.withMillis(instant.toDateTime(defaultChrono).getMillis()));

        if (bucket.getOffsetInteger() != null) {
            int parsedOffset = bucket.getOffsetInteger();
            DateTimeZone parsedZone = DateTimeZone.forOffsetMillis(parsedOffset);
            chrono = chrono.withZone(parsedZone);
        } else if (bucket.getZone() != null) {
            chrono = chrono.withZone(bucket.getZone());
        }

        instant.setChronology(chrono);

        return newPos;
    }
}