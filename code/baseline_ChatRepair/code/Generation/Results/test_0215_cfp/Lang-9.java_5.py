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
            if (endQuote == -1) {
                throw new IllegalArgumentException("Invalid pattern: Unterminated quote");
            }
            // move matcher past the end quote
            patternMatcher.region(endQuote + 1, pattern.length());
        } else {
            String currentFormatField = patternMatcher.group();
            Strategy currentStrategy = getStrategy(currentFormatField);
            if (currentStrategy.addRegex(this, regex)) {
                collector.add(currentStrategy);
            }
        }
    }

    if (!collector.isEmpty()) {
        Strategy lastStrategy = collector.get(collector.size() - 1);
        if (lastStrategy.addRegex(this, regex)) {
            // ensure the last strategy's regex part is added
            collector.add(lastStrategy);
        }
    }

    strategies = (Strategy[]) collector.toArray(new Strategy[collector.size()]); // Explicit cast and array creation
    parsePattern = Pattern.compile(regex.toString());
}
