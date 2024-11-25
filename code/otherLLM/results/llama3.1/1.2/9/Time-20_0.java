public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    // Input validation: Ensure that the input `text` parameter is not empty.
    if (text == null || text.isEmpty()) {
        return -1; // Return a more informative value when the input string is empty.
    }
    
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        // Case sensitivity handling: Compare with `ALL_IDS` in a case-insensitive manner.
        if (str.startsWith(id, StringComparison.OrdinalIgnoreCase)) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    
    // Error handling: Return a more informative value when the time zone ID is not found.
    return -1;
}