private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    boolean isQuoted = false; // To track if we are inside a quoted section

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
        nextStrategy = getStrategy(nextFormatField);
        
        // Check if the current format field is quoted
        if (currentFormatField.startsWith("\"") && !currentFormatField.endsWith("\"")) {
            throw new IllegalArgumentException("Unterminated quote in regex pattern");
        }
        // Add a quote check for the next format field if it's also quoted
        boolean isNextQuoted = nextFormatField.startsWith("\"") && nextFormatField.endsWith("\"");

        if (currentStrategy.addRegex(this, regex, isQuoted)) {
            collector.add(currentStrategy);
        }

        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
        // Update the isQuoted flag
        isQuoted = isNextQuoted;
    }
    
    if (currentStrategy.addRegex(this, regex, isQuoted)) {
        collector.add(currentStrategy);
    }
    
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}