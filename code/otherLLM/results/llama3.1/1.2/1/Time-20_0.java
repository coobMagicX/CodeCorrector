public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        ZonedDateTime dateTime = ZonedDateTime.parse(text.substring(position), formatter);
        if (bucket.getZone() != null && !dateTime.getZone().getId().equals(bucket.getZone())) {
            bucket.setZone(dateTime.getZone());
        }
        return position + text.substring(position).length();
    } catch (DateTimeParseException e) {
        // Return the original position with an informative message
        return ~position;
    }
}