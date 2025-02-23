private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();

    Matcher patternMatcher = formatPattern.matcher(pattern);
    int lastEnd = 0; // Track end of the last match to determine literals
    Strategy currentStrategy;

    while (patternMatcher.find()) {
        String literal = pattern.substring(lastEnd, patternMatcher.start());
        if (!literal.isEmpty()) {
            quoteRegexLiteral(regex, literal);
        }
        
        String token = patternMatcher.group();
        currentStrategy = getStrategy(token);
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        
        lastEnd = patternMatcher.end();
    }

    // Handle trailing literal text after the last pattern segment
    if (lastEnd < pattern.length()) {
        String trailingLiteral = pattern.substring(lastEnd);
        quoteRegexLiteral(regex, trailingLiteral);
    }

    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}

/**
 * Method to append a literal string as explicitly quoted text to the regex.
 */
private void quoteRegexLiteral(StringBuilder regex, String literal) {
    regex.append(Pattern.quote(literal));
}
