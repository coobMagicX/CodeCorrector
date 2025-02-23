private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();

    Matcher patternMatcher = formatPattern.matcher(pattern);

    boolean inQuotes = false;

    while (patternMatcher.find()) {
        String match = patternMatcher.group();
        if ("'".equals(match)) {
            inQuotes = !inQuotes;  // Toggle quote status
            continue;
        }
        if (!inQuotes && !match.isEmpty()) {
            Strategy strategy = getStrategy(match);
            if (strategy.addRegex(this, regex)) {
                collector.add(strategy);
            }
        } else {
            regex.append(Pattern.quote(match));  // Add literal text between quotes
        }
    }
    
    if (inQuotes) {
        throw new IllegalArgumentException("Unmatched quotes in format pattern");
    }

    // Add last strategy if exists
    if (!collector.isEmpty() && collector.get(collector.size() - 1).addRegex(this, regex)) {
        collector.add(collector.get(collector.size() - 1));
    }

    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}
