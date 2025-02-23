private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);
    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();

    int len = pattern.length();
    boolean inQuote = false;
    StringBuilder buffer = new StringBuilder();

    for (int i = 0; i < len; i++) {
        char ch = pattern.charAt(i);
        if (ch == '\'') {
            if (inQuote) {
                if (i + 1 < len && pattern.charAt(i + 1) == '\'') {
                    // An escaped quote ('')
                    buffer.append(ch);
                    i++; // Skip the next quote
                } else {
                    // End of quote
                    inQuote = false;
                    if (buffer.length() > 0) {
                        // Add literal text as fixed strategy
                        collector.add(new Strategy.FixedStrategy(buffer.toString()));
                        buffer.setLength(0); // reset buffer
                    }
                }
            } else {
                if (buffer.length() > 0) {
                    // Add the buffer as a strategy before starting quote
                    Strategy strat = getStrategy(buffer.toString());
                    if (strat.addRegex(this, regex)) {
                        collector.add(strat);
                    }
                    buffer.setLength(0); // reset buffer
                }
                inQuote = true;
            }
        } else {
            buffer.append(ch);
        }
    }

    // Handle any remaining characters
    if (buffer.length() > 0) {
        if (inQuote) {
            throw new IllegalArgumentException("Unterminated quote in date format pattern");
        }
        Strategy strat = getStrategy(buffer.toString());
        if (strat.addRegex(this, regex)) {
            collector.add(strat);
        }
    }
    
    strategies = collector.toArray(new Strategy[collector.size()]);
    parsePattern = Pattern.compile(regex.toString());
}
