public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}

Methods that you can utilize:
public DateTimeFormatterBuilder appendTimeZoneId() {
    return append0(TimeZoneId.INSTANCE, TimeZoneId.INSTANCE);
}