private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    boolean hasUnquotedStart = false;
    
    Matcher patternMatcher = formatPattern.matcher(pattern);
    if (!patternMatcher.lookingAt()) {
        throw new IllegalArgumentException("Invalid pattern");
    }

    currentFormatField = patternMatcher.group();
    Strategy currentStrategy = getStrategy(currentFormatField);

    while (true) {
        // Check for quoted characters and ensure they are properly closed
        int start = patternMatcher.start();
        int end = patternMatcher.end();
        if (!hasUnquotedStart && currentStrategy instanceof CopyQuotedStrategy) {
            boolean isQuoted = false;
            for (int i = start; i < end; i++) {
                char c = patternMatcher.pattern().charAt(i);
                if (c == '\'') {
                    isQuoted = !isQuoted;
                }
            }
            if (!isQuoted) {
                throw new IllegalArgumentException("Invalid pattern: unterminated quote");
            }
        }

        patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
        if (!patternMatcher.lookingAt()) {
            nextStrategy = null;
            break;
        }
        String nextFormatField = patternMatcher.group();
        nextStrategy = getStrategy(nextFormatField);
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        
        // Update currentFormatField and currentStrategy
        hasUnquotedStart = !((CopyQuotedStrategy) currentStrategy).isUnquoted();
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
    }

    if (currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    
    // Reset variables
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}