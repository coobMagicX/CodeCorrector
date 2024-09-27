public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    // Append TimeZoneId to DateTimeFormatterBuilder
    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
    builder.appendTimeZoneId();
    DateTimeFormatter formatter = builder.toFormatter();
    // Parse the TimeZoneId from the input text
    ParsePosition parsePosition = new ParsePosition(position);
    TimeZone timeZone = formatter.parse(str, parsePosition);
    if (timeZone != null) {
        bucket.setZone(DateTimeZone.forTimeZone(timeZone));
        return parsePosition.getIndex();
    }
    return ~position;
}