private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    Matcher patternMatcher = formatPattern.matcher(pattern);
    
    while (patternMatcher.find()) {
        String currentFormatField = patternMatcher.group();
        Strategy currentStrategy = getStrategy(currentFormatField, patternMatcher.start(), patternMatcher.end());

        if (collector.isEmpty()) {
            // If first strategy, add it to the collector
            collector.add(currentStrategy);
            currentStrategy.addRegex(this, regex);
        } else {
            // Check if the next strategy seamlessly continues from the last (numeric patterns)
            Strategy lastStrategy = collector.get(collector.size() - 1);
            if (lastStrategy.isNumberBased() && currentStrategy.isNumberBased()) {
                currentStrategy.addRegex(this, regex, lastStrategy.getMaxLength());
            } else {
                currentStrategy.addRegex(this, regex);
            }
            collector.add(currentStrategy);
        }
    }

    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}

private Strategy getStrategy(String pattern, int start, int end) {
    // This is a stub that should be replaced by the actual implementation that picks a specific parsing/formatter strategy.
    // Typically it will look at the pattern and decide whether it's a Year, Month, Day etc., and will also potentially consider positions `start` and `end` to identify overlaps.
    // You might use these indices to ensure proper lengths in regexes for numeric fields, e.g., YY vs YYYY.
    // For example:
    // If pattern is "YYYY", return a Year strategy with a 4-digit regex pattern.
    // If pattern is "YY", return a Year strategy with a 2-digit regex pattern.
    return new Strategy(pattern); // Placeholder
}

class Strategy {
    private String pattern;
    Strategy(String pattern) {
        this.pattern = pattern;
    }

    public boolean isNumberBased() {
        return pattern.matches("\\d+");
    }

    public int getMaxLength() {
        return pattern.length();
    }

    public boolean addRegex(Appendable regex, StringBuilder existingRegex) {
        // Here you might need to adjust existing regex patterns based on whether previous strategy was number based or not.
        return true;
    }
    
    public boolean addRegex(Appendable regex, StringBuilder existingRegex, int otherMaxLength) {
        // Adjustments for continuous numeric fields without separators.
        return true;
    }
}
