private void init() {
    thisYear= Calendar.getInstance(timeZone, locale).get(Calendar.YEAR);

    nameValues = new ConcurrentHashMap<Integer, KeyValue[]>();

    StringBuilder regex = new StringBuilder();
    List<Strategy> collector = new ArrayList<Strategy>();

    Matcher patternMatcher = formatPattern.matcher(pattern);

    while (patternMatcher.find()) {
        String token = patternMatcher.group();
        if (token.startsWith("'")) {
            if (token.length() == 1 || !token.endsWith("'")) {
                throw new IllegalArgumentException("Invalid pattern, unterminated quote: " + token);
            }
            // Tokens wrapped in single quotes are treated as literals and quoted in regex
            regex.append(Pattern.quote(token.substring(1, token.length() - 1)));
            continue;
        }

        Strategy strategy = getStrategy(token);
        if (strategy.addRegex(this, regex)) {
            collector.add(strategy);
        }
    }

    if (!collector.isEmpty()) {
        strategies = collector.toArray(new Strategy[collector.size()]);
        parsePattern = Pattern.compile(regex.toString());
    } else {
        throw new IllegalArgumentException("No valid strategy found for provided pattern");
    }
}
