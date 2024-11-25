private void init() {
    thisYear= Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues= new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex= new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    
    // Validate input pattern before parsing
    if (pattern == null || pattern.isEmpty()) {
        throw new IllegalArgumentException("Invalid or empty pattern");
    }

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
        
        // Ensure proper handling of unterminated quotes
        String nextFormatField= patternMatcher.group();
        if (nextFormatField.length() > 0 && (nextFormatField.charAt(0) == '\'' || nextFormatField.charAt(0) == '"')) {
            int quoteChar = nextFormatField.charAt(0);
            int openQuoteIndex = -1;
            for (int i = 0; i < nextFormatField.length(); i++) {
                if (nextFormatField.charAt(i) == quoteChar && (openQuoteIndex == -1 || nextFormatField.charAt(openQuoteIndex) != quoteChar)) {
                    openQuoteIndex = i;
                } else if (nextFormatField.charAt(i) == quoteChar && nextFormatField.charAt(openQuoteIndex) == quoteChar) {
                    nextFormatField = nextFormatField.substring(0, openQuoteIndex + 1) + nextFormatField.substring(openQuoteIndex + 2);
                    break;
                }
            }
        }

        nextStrategy = getStrategy(nextFormatField);
        if(currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField= nextFormatField;
        currentStrategy= nextStrategy;
    }
    
    // Address edge cases for empty or malformed patterns
    if (currentStrategy != null && currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }

    currentFormatField= null;
    strategies= collector.toArray(new Strategy[collector.size()]);
    parsePattern= Pattern.compile(regex.toString());
}