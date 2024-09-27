public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String str = text.substring(position);
    List<String> sortedIds = new ArrayList<>(ALL_IDS);
    sortedIds.sort((id1, id2) -> Integer.compare(id2.length(), id1.length())); // Sort by length in descending order
    for (String id : sortedIds) {
        if (str.startsWith(id)) {
            bucket.setZone(DateTimeZone.forID(id));
            return position + id.length();
        }
    }
    return ~position;
}