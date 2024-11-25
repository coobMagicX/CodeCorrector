public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    
    // Create a DateTimeFormatter with all possible time zone IDs
    DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
    for (String id : ALL_IDS) {
        builder.appendTimeZoneId();
        builder.appendLiteral(id).appendOptional(' ');
    }
    DateTimeFormatter formatter = builder.toFormatter();

    // Parse the string using the formatter to find a match
    TemporalAccessor accessor = formatter.parseUnresolved(str);

    if (accessor != null && accessor.isSupported(TemporalField.ofType(ZoneId.class))) {
        ZoneId zoneId = (ZoneId) accessor.get(TemporalField.ofType(ZoneId.class));
        bucket.setZone(zoneId);
        return position + str.indexOf(zoneId.getId()); // Adjust to use the index of the zone ID in the string
    }

    return ~position;
}