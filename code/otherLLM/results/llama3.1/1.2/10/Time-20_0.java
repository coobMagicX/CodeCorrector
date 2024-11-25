public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    // Normalize and validate the input time zone ID to match the expected format.
    if (str.startsWith("GMT") || str.startsWith("UTC")) {
        bucket.setZone(DateTimeZone.forID(str.toUpperCase()));
        return position + str.length();
    } else if (str.matches("[A-Z]{3}")) {
        // Consider using a more robust parsing approach, such as using a regular expression or a dedicated parsing library.
        String id = DateTimeZone.getTimeZone(str).getID();
        bucket.setZone(DateTimeZone.forID(id));
        return position + str.length();
    }
    // Review the logic for returning the position after parsing; it may need adjustment to account for the nuances of date-time string parsing.
    return ~position;
}