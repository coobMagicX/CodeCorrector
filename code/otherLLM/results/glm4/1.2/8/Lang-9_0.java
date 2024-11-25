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
    regex.append(currentFormatField); // Append the first matched field to ensure it's included
    for (int i = 0; patternMatcher.find(); i++) {
        String nextFormatField = patternMatcher.group();
        if (!nextFormatField.startsWith("\"") || !nextFormatField.endsWith("\"")) {
            throw new IllegalArgumentException("Unterminated quote in pattern string");
        }
        nextFormatField = nextFormatField.replaceAll("^\"|\"$", ""); // Remove the quotes from both ends

        Strategy nextStrategy = getStrategy(nextFormatField);
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }

        currentFormatField = nextFormatField;
        currentStrategy = nextStrategy;

        // Append the new field without quotes to the regex
        regex.append("|").append(nextFormatField); // Ensure it's added with a delimiter
    }
    
    if (currentStrategy.addRegex(this, regex)) {
        collector.add(currentStrategy);
    }

    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}