import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeParser {
    private static final String[] ALL_IDS = { "America/Dawson_Creek" }; // Assuming this is the array of all IDs

    public int parseInto(DateTimeParserBucket bucket, String text, int position) {
        String str = text.substring(position);
        for (String id : ALL_IDS) {
            if (str.startsWith(id)) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendTimeZoneId() // Appends the time zone ID to the formatter
                        .append(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toFormatter();
                bucket.setZone(DateTimeZone.forID(id));
                return position + id.length();
            }
        }
        return ~position; // Using bitwise complement to indicate error or end of parsing
    }

    public static void main(String[] args) {
        DateTimeParser parser = new DateTimeParser();
        DateTimeParserBucket bucket = new DateTimeParserBucket();

        String dateStr = "2023-03-14 15:30:00 America/Dawson_Creek";
        int newPosition = parser.parseInto(bucket, dateStr, 0);

        if (newPosition >= 0) {
            System.out.println("Parsed position: " + newPosition);
            System.out.println("Time zone ID: " + bucket.getZone().getId());
        } else {
            System.out.println("Failed to parse the date string.");
        }
    }
}

// Assuming DateTimeParserBucket and related methods exist as per Joda-Time library
class DateTimeParserBucket {
    private DateTimeZone zone;

    public void setZone(DateTimeZone zone) {
        this.zone = zone;
    }

    public DateTimeZone getZone() {
        return zone;
    }
}