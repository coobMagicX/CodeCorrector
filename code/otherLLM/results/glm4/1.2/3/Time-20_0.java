public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    // The original method does not handle the parsing of a full date-time string with a zone ID.
    // We need to use DateTimeFormatterBuilder and DateTimeFormatter to parse the full string.

    // Create a DateTimeFormatter that includes both date and time, as well as the zone ID
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatUtils.DATE_TIME_FORMAT)
            .appendTimeZoneId() // This method appends the timeZoneId component
            .toFormatter();

    try {
        // Parse the text starting at the given position
        ZonedDateTime zdt = ZonedDateTime.parse(text.substring(position), formatter);
        
        // Set the zone ID on the bucket and return the new position after the date-time string
        bucket.setZone(DateTimeZone.forID(zdt.getZone().getId()));
        return position + zdt.toString().length();
    } catch (DateTimeParseException e) {
        // If parsing fails, return ~position as an indication of failure
        return ~position;
    }
}