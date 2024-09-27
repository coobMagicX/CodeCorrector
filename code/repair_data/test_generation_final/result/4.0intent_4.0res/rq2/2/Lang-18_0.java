protected List<Rule> parsePattern() {
    DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
    List<Rule> rules = new ArrayList<Rule>();

    String[] ERAs = symbols.getEras();
    String[] months = symbols.getMonths();
    String[] shortMonths = symbols.getShortMonths();
    String[] weekdays = symbols.getWeekdays();
    String[] shortWeekdays = symbols.getShortWeekdays();
    String[] AmPmStrings = symbols.getAmPmStrings();

    int length = mPattern.length();
    int[] indexRef = new int[1];

    for (int i = 0; i < length; i++) {
        indexRef[0] = i;
        String token = parseToken(mPattern, indexRef);
        i = indexRef[0];

        int tokenLen = token.length();
        if (tokenLen == 0) {
            break;
        }

        Rule rule;
        char c = token.charAt(0);

        switch (c) {
        // Other cases remain unchanged

        case '\'': // literal text
            // Correctly parse literal text that might contain pattern characters
            StringBuilder literal = new StringBuilder();
            i++; // Skip the opening quote
            for (; i < length; i++) {
                char ch = mPattern.charAt(i);
                if (ch == '\'') {
                    if (i + 1 < length && mPattern.charAt(i + 1) == '\'') {
                        // Handle escaped quote
                        literal.append(ch);
                        i++; // Skip the escaped quote
                    } else {
                        break; // End of literal
                    }
                } else {
                    literal.append(ch);
                }
            }
            if (literal.length() == 1) {
                rule = new CharacterLiteral(literal.charAt(0));
            } else {
                rule = new StringLiteral(literal.toString());
            }
            break;

        // Other cases remain unchanged
        }

        rules.add(rule);
    }

    return rules;
}