private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();

    Matcher patternMatcher = formatPattern.matcher(pattern);
    if (!patternMatcher.lookingAt()) {
        throw new IllegalArgumentException("Invalid pattern");
    }

    currentFormatField = patternMatcher.group();
    Strategy currentStrategy = getStrategy(currentFormatField);
    for (;;) {
        patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
        if (!patternMatcher.lookingAt()) {
            nextStrategy = null;
            break;
        }
        String nextFormatField = patternMatcher.group();
        
        // Check if the quote is terminated
        if (nextFormatField.startsWith("\"") && !nextFormatField.endsWith("\"")) {
            nextFormatField = nextFormatField.substring(1);
            currentFormatField = nextFormatField;
            continue;
        }

        nextStrategy = getStrategy(nextFormatField);
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
    }
    
    // Check if the final strategy is added
    if (currentFormatField != null && currentStrategy != null) {
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
    } else {
        // Handle unterminated quotes or no strategies found
    }

    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}