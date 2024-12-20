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
        // Validate input strings for unbalanced quotes
        if (currentFormatField.startsWith("\"") && !currentFormatField.endsWith("\"")) {
            throw new IllegalArgumentException("Unterminated quote in pattern");
        }
        
        patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
        if(!patternMatcher.lookingAt()) {
            nextStrategy = null;
            break;
        }
        String nextFormatField= patternMatcher.group();
        
        // Handle unterminated quotes
        if (nextFormatField.startsWith("\"") && !nextFormatField.endsWith("\"")) {
            throw new IllegalArgumentException("Unterminated quote in pattern");
        }

        nextStrategy = getStrategy(nextFormatField);
        if(currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField= nextFormatField;
        currentStrategy= nextStrategy;
    }
    
    // Ensure proper handling of the last strategy
    if (currentStrategy != null && currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }

    currentFormatField= null;
    strategies= collector.toArray(new Strategy[collector.size()]);
    parsePattern= Pattern.compile(regex.toString());
}