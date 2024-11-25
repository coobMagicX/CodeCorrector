private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<>();

    Matcher patternMatcher = formatPattern.matcher(pattern);
    if (!patternMatcher.lookingAt()) {
        throw new IllegalArgumentException("Invalid pattern");
    }

    currentFormatField = patternMatcher.group();
    Strategy currentStrategy = getStrategy(currentFormatField);
    for (int i = 0; i < collector.size(); i++) {
        regex.append("(?<!\\\\)").append(currentFormatField); // Avoid double escaping
        if (currentStrategy.addRegex(this, regex)) {
            collector.add(currentStrategy);
        }
    }

    currentFormatField = null;
    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}

// Add a helper method to check for unterminated quotes in the pattern
private boolean isTerminated(String pattern) {
    int len = pattern.length();
    int balance = 0;
    for (int i = 0; i < len; ++i) {
        char ch = pattern.charAt(i);
        if (ch == '\"') {
            balance += 1 - (balance % 2);
        } else if ((ch == '(' || ch == '[') && balance > 0) {
            balance++;
        } else if ((ch == ')' || ch == ']') && balance > 1) {
            balance--;
        }
    }
    return balance == 0;
}

// Use the helper method before parsing
if (!isTerminated(pattern)) {
    throw new IllegalArgumentException("Invalid pattern: Unterminated quote detected");
}