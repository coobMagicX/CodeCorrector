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
        nextStrategy = getStrategy(nextFormatField);

        // Check if the regex is being closed properly before adding to collector
        boolean isClosed = false;
        try {
            Pattern.compile(regex.toString());
            isClosed = true;
        } catch (PatternSyntaxException e) {
            // This means the regex was not valid, hence it's not closed
            break;
        }

        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
    }
    if (currentStrategy != null && currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}

// Use this method to retrieve the compiled pattern
public Pattern getParsePattern() {
    return parsePattern;
}