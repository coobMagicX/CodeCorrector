private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    boolean inQuotes = false;

    Matcher patternMatcher = formatPattern.matcher(pattern);
    if (!patternMatcher.lookingAt()) {
        throw new IllegalArgumentException("Invalid pattern");
    }

    currentFormatField = patternMatcher.group();
    Strategy currentStrategy = getStrategy(currentFormatField);

    // Corrected the loop to handle quotes and ensure proper region handling
    while (true) {
        if (!inQuotes && !patternMatcher.lookingAt()) {
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

        // Check for quote characters to toggle the inQuotes flag
        if (!inQuotes && patternMatcher.quote() == 0) {
            inQuotes = true;
        } else if (inQuotes && patternMatcher.quote() > 0) {
            inQuotes = false;
        }

        // Move the region of the matcher after the current quote or non-quoted character
        int end = patternMatcher.regionEnd();
        while (pattern.length() > end && (pattern.charAt(end) == '\'' || pattern.charAt(end) == '"')) {
            end++;
        }
        patternMatcher.region(patternMatcher.end(), end);
    }

    if (currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }
    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]); // Fixed size calculation
    parsePattern = Pattern.compile(regex.toString());
}