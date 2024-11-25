protected List<Rule> parsePattern() {
    DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
    List<Rule> rules = new ArrayList<Rule>();

    // Extract arrays from DateFormatSymbols for various components like eras, months, weekdays, etc.
    String[] ERAs = symbols.getEras();
    String[] months = symbols.getMonths();
    String[] shortMonths = symbols.getShortMonths();
    String[] weekdays = symbols.getWeekdays();
    String[] shortWeekdays = symbols.getShortWeekdays();
    String[] AmPmStrings = symbols.getAmPmStrings();

    int length = mPattern.length(); // The length of the format string
    int[] indexRef = new int[1]; // Array to hold the current index for token extraction

    for (int i = 0; i < length; i++) {
        indexRef[0] = i;
        String token = parseToken(mPattern, indexRef); // Extract a token from the pattern string
        if (token.length() == 0) { // If the token is empty, exit the loop
            break;
        }

        Rule rule;
        char c = token.charAt(0);

        switch (c) {
            case 'G': // Era designator
                rule = new TextField(Calendar.ERA, ERAs);
                break;
            case 'y': // Year
                if (token.length() >= 4) {
                    rule = selectNumberRule(Calendar.YEAR, token.length());
                } else {
                    rule = TwoDigitYearField.INSTANCE;
                }
                break;
            // ... Other cases ...
            case '\'':
                String sub = token.substring(1);
                if (sub.length() == 1) {
                    rule = new CharacterLiteral(sub.charAt(0));
                } else {
                    rule = new StringLiteral(sub);
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal pattern component: " + token);
        }

        rules.add(rule); // Add the created rule to the list
    }

    return rules; // Return the list of rules
}