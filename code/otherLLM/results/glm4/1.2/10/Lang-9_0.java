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
        int start = patternMatcher.end();
        String nextFormatField;
        try {
            nextFormatField = patternMatcher.find(start) ? patternMatcher.group() : null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid pattern", e);
        }

        if (nextFormatField == null) {
            break;
        }

        Strategy nextStrategy = getStrategy(nextFormatField);
        currentStrategy.addRegex(this, regex);

        // Check for unterminated quotes by ensuring that the format field ends correctly
        if (currentStrategy instanceof CopyQuotedStrategy && !((CopyQuotedStrategy) currentStrategy).isTerminated()) {
            throw new IllegalArgumentException("Invalid pattern: unterminated quote found in field");
        }

        collector.add(currentStrategy);
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
    }
    if (currentStrategy != null) {
        currentStrategy.addRegex(this, regex);

        // Check for unterminated quotes by ensuring that the last format field ends correctly
        if (currentStrategy instanceof CopyQuotedStrategy && !((CopyQuotedStrategy) currentStrategy).isTerminated()) {
            throw new IllegalArgumentException("Invalid pattern: unterminated quote found in last field");
        }
    }

    collector.add(currentStrategy);
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}