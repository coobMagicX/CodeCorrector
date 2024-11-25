import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;

public class DateTimeParser {

    private static final List<String> ALL_IDS = Arrays.asList(
            "America/New_York",
            "America/Chicago",
            "America/Denver",
            "America/Los_Angeles",
            "America/Anchorage",
            "America/Phoenix",
            "Europe/Paris",
            "Asia/Tokyo",
            "Asia/Shanghai",
            "Asia/Kolkata"
    );

    public static void main(String[] args) {
        DateTimeParser parser = new DateTimeParser();
        DateTimeParserBucket bucket = new DateTimeParserBucket();
        String text = "2023-04-01T14:00:00Z";
        int position = 0;

        System.out.println("Parsed position: " + parser.parseInto(bucket, text, position));
    }

    public int parseInto(DateTimeParserBucket bucket, String text, int position) {
        String str = text.substring(position);
        
        // Sort ALL_IDS by length in descending order to prioritize longer matching IDs
        Collections.sort(ALL_IDS, Comparator.comparingInt(String::length).reversed());

        for (String id : ALL_IDS) {
            if (str.startsWith(id)) {
                bucket.setZone(ZoneId.of(id));
                return position + id.length();
            }
        }

        return ~position;
    }

    // Additional method to use as needed
    public DateTimeFormatterBuilder appendTimeZoneId() {
        return new DateTimeFormatterBuilder().appendTimeZoneId();
    }
}

// Dummy classes for the context of this task, assuming their existence:
class DateTimeParserBucket {
    private ZoneId zone;

    public void setZone(ZoneId zone) {
        this.zone = zone;
    }

    public ZoneId getZone() {
        return zone;
    }
}