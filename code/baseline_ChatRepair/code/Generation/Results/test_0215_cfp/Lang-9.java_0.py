private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();
    Matcher patternMatcher = formatPattern.matcher(pattern);

    boolean inQuotes = false;
    int lastPatternEnd = 0;

    while (patternMatcher.find()) {
        // Check for text quoted outside of pattern
        if(inQuotes) {
            // End quote reached; process quoted text as literal
            if(pattern.substring(patternMatcher.start(), patternMatcher.end()).equals("'")) {
                regex.append(Pattern.quote(pattern.substring(lastPatternEnd, patternMatcher.start())));
                inQuotes = false;
                lastPatternEnd = patternMatcher.end();
            }
            continue;
        }
        
        // Check for the start of a quote
        if(pattern.substring(patternMatcher.start(), patternMatcher.end()).equals("'")) {
            // Add regex for everything before the quote as a strategy
            if(patternMatcher.start() != lastPatternEnd) {
                String nonQuotedText = pattern.substring(lastPatternEnd, patternMatcher.start());
                Strategy strategy = getStrategy(nonQuotedText);
                if(strategy.addRegex(this, regex)) {
                    collector.add(strategy);
                }
            }
            inQuotes = true;
            lastPatternEnd = patternMatcher.end();
            continue;
        }

        // Regular processing
        String currentFormatField = patternMatcher.group();
        Strategy currentStrategy = getStrategy(currentFormatField);
        if(currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
        lastPatternEnd = patternMatcher.end();
    }

    // Handle remaining characters after the last regex match
    if(lastPatternEnd < pattern.length()) {
        if(inQuotes) {
            // Unterminated quote detected
            throw new IllegalArgumentException("Unterminated quote in format pattern.");
        } else {
            regex.append(Pattern.quote(pattern.substring(lastPatternEnd)));
        }
    }

    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}
