public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    // Check if input string matches a known pattern and format
    if (str.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}[Tt][0-9]{2}:[0-9]{2}:[0-9]{2}(?:\\.[0-9]+)?(?:[Zz]|[+-][0-9]{2}:[0-9]{2})$")) {
        // Extract zone ID from date-time string
        String zoneId = str.substring(str.length() - 6); // Assuming zone ID is always 6 characters long (e.g., 'UTC', '+08:00')
        
        // Validate extracted zone ID against known IDs in ALL_IDS list
        for (String id : ALL_IDS) {
            if (zoneId.equals(id)) {
                bucket.setZone(DateTimeZone.forID(id));
                return position + str.length();
            }
        }
    }
    
    // If input string does not match a known pattern or format, or zone ID is invalid, return failure
    return ~position;
}