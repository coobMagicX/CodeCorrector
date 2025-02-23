public int parseInto(DateTimeParserBucket bucket, String text, int position) {
    String remainingText = text.substring(position);

    
    
    List<String> sortedIds = Arrays.asList(ALL_IDS);
    Collections.sort(sortedIds, (a, b) -> b.length() - a.length());

    
