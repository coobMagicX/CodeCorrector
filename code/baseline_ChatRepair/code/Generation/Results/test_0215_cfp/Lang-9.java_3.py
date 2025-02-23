private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<>();

    Matcher patternMatcher = formatPattern.matcher(pattern);
    int patternLength = pattern.length();
    while (patternMatcher.regionStart() < patternLength) {
        if (pattern.charAt(patternMatcher.regionStart()) == '\'') {
            // Find the end of the quoted section
            int index = patternMatcher.regionStart() + 1;
            while (index < patternLength && pattern.charAt(index) != '\'') {
                index++;
            }
            if (index >= patternLength) {
                throw new IllegalArgumentException("Invalid pattern: Unterminated quote");
            }
            index++; // Move past the end quote
            patternMatcher.region(index, patternMatcher.regionEnd());
        } else {
            if (!patternMatcher.lookingAt()) {
                break;
            }
            String currentFormatField = patternMatcher.group();
            Strategy currentStrategy = getStrategy(currentFormatField);
            if (currentStrategy != null && currentStrategy.addRegex(this, regex)) {
                collector.add(currentStrategy);
            }
            patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
        }
    }

    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}
