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
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;

        // Added check to ensure we don't process the same strategy twice
        if (nextStrategy == currentStrategy) {
            break;
        }
    }

    // Check if the last strategy's addRegex method returns true before adding it to the collector
    if (currentStrategy != null && currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}