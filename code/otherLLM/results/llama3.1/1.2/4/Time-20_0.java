public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            builder.appendZoneId();
            break;
        }
    }
    DateTimeFormatter formatter = builder.build();

    // Validate input format
    try {
        Date date = formatter.parse(str);
        bucket.setZone(DateTimeZone.forID(date.getTimezone()));
        return position + id.length();
    } catch (DateTimeParseException e) {
        // Handle parsing exception
    }

    // Handle edge cases and overlapping IDs
    for (String overlap : OVERLAPPING_IDS) {
        if (str.contains(overlap)) {
            bucket.setZone(DateTimeZone.forID("UTC"));
            return position + overlap.length();
        }
    }

    return ~position;
}