private void init() {
    thisYear= Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues= new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex= new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();

    Matcher patternMatcher= formatPattern.matcher(pattern);
    if(!patternMatcher.lookingAt()) {
        throw new IllegalArgumentException("Invalid pattern");
    }

    currentFormatField= patternMatcher.group();
    Strategy currentStrategy= getStrategy(currentFormatField);
    for(;;) {
        patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
        if(!patternMatcher.lookingAt()) {
            nextStrategy = null;
            break;
        }
        String nextFormatField= patternMatcher.group();
        
        // Validate quote syntax
        int quotes = countQuotes(nextFormatField);
        if(quotes % 2 != 0) {
            throw new IllegalArgumentException("Unbalanced quotes in pattern");
        }

        nextStrategy = getStrategy(nextFormatField);
        if(currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField= nextFormatField;
        currentStrategy= nextStrategy;
    }
    
    // Update regular expression construction
    if(currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    currentFormatField= null;
    strategies= collector.toArray(new Strategy[collector.size()]);
    
    // Re-compile the pattern with the updated regex
    parsePattern= Pattern.compile(regex.toString());
}

// Helper method to count quotes in a string
private int countQuotes(String str) {
    int count = 0;
    for(int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        if(c == '"' || c == '\'') {
            count++;
        }
    }
    return count;
}