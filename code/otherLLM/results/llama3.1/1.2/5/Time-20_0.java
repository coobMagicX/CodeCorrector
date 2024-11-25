public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    for (String id : ALL_IDS) {
        if (str.startsWith(id)) { // Check if the current substring starts with a time zone ID
            DateTimeZone tz = DateTimeZone.forID(id); // Get the time zone object from the ID
            if (!tz.isAmbiguous()) { // Check if the time zone is not ambiguous
                bucket.setZone(tz);
                return position + id.length(); // Return the updated position
            } else {
                String[] ambiguousIds = tz.getAmbiguousIDs(); // Get the list of ambiguous IDs for this time zone
                for (String ambiguousId : ambiguousIds) { 
                    if (str.startsWith(ambiguousId)) { // Check if the current substring starts with an ambiguous ID
                        bucket.setZone(tz);
                        return position + id.length();
                    }
                }
            }
        }
    }
    return ~position;
}