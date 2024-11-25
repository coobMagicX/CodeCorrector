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

        // Check if the next format field is properly closed with a quote
        if (nextFormatField.startsWith("\"") && !nextFormatField.endsWith("\"")) {
            throw new IllegalArgumentException("Unterminated quote in pattern");
        }

        nextStrategy = getStrategy(nextFormatField);
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;
    }
    if (currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]); // Use correct array size
    parsePattern = Pattern.compile(regex.toString());
}