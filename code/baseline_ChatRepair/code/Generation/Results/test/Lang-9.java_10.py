private void init() {
    thisYear = Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<>();

    Matcher patternMatcher = formatPattern.matcher(pattern);
    boolean inQuotes = false;
    StringBuilder literal = new StringBuilder();

    while (patternMatcher.find()) {
        String match = patternMatcher.group();

        if (match.equals("'")) {
            // Toggle the quotation status
            if (inQuotes && literal.length() > 0) {
                regex.append(Pattern.quote(literal.toString()));
                literal.setLength(0); // reset the StringBuilder
            }
            inQuotes = !inQuotes;
            continue;
        }

        if (inQuotes) {
            literal.append(match); // Collect literal text into a StringBuilder
        } else {
            if (literal.length() > 0) {
                regex.append(Pattern.quote(literal.toString()));
                literal.setLength(0);
            }
            Strategy strategy = getStrategy(match);
            if (strategy != null && strategy.addRegex(this, regex)) {
                collector.add(strategy);
            } else {
                throw new IllegalArgumentException("Unknown pattern character: " + match);
            }
        }
    }

    // If ended while still in quotes, append the last literal
    if (inQuotes && literal.length() > 0) {
        regex.append(Pattern.quote(literal.toString()));
    }

    // Handle any strategy parsing after the last recognized pattern
    if (!collector.isEmpty() && collector.get(collector.size() - 1).addRegex(this, regex)) {
        collector.add(collector.get(collector.size() - 1));
    }

    strategies = collector.toArray(new Strategy[0]);
    parsePattern = Pattern.compile(regex.toString());
}
