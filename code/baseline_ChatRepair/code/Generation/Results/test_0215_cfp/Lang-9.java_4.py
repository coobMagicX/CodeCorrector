private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>(); // Assuming KeyValue is a defined class with appropriate constructors

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>(); // Explicit generic type

    Matcher patternMatcher = formatPattern.matcher(pattern);
    while (patternMatcher.find()) {
        if (pattern.charAt(patternMatcher.start()) == '\'') {
            // skip quoted texts
            int endQuote = pattern.indexOf("'", patternMatcher.end());
            if (endQuote == -1 || endQuote == patternMatcher.end()) {
                throw new IllegalArgumentException("Invalid pattern: Unterminated quote");
            }
            patternMatcher.region(endQuote + 1, pattern.length());
        } else {
            String currentFormatField = patternMatcher.group();
            Strategy currentStrategy = getStrategy(currentFormatField, patternMatcher.start(), patternMatcher.end());
            if (currentStrategy.addRegex(this, regex)) {
                collector.add(currentStrategy);
            }
        }
    }

    strategies = (Strategy[]) collector.toArray(new Strategy[collector.size()]); // Explicit cast and array creation
    parsePattern = Pattern.compile(regex.toString());
}
