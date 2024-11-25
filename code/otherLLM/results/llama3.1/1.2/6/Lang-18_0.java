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
        case 'G': // era designator (text)
            rule = new TextField(Calendar.ERA, ERAs);
            break;
        case 'y': // year (number)
            if (tokenLen >= 4) {
                rule = selectNumberRule(Calendar.YEAR, tokenLen);
            } else {
                rule = TwoDigitYearField.INSTANCE;
            }
            break;
        case 'M': // month in year (text and number)
            if (token.contains("MMM")) { 
                String[] monthNames = token.split("'");
                rule = new MonthNameRule(mLocale, monthNames);
            } else {
                if (tokenLen >= 3) {
                    rule = selectNumberRule(Calendar.MONTH, tokenLen);
                } else {
                    rule = new MonthLiteral(token.charAt(0));
                }
            }
            break;
        case 'a': // am/pm marker (text)
            rule = new TextField(Calendar.AM_PM, AmPmStrings);
            break;
        case '\'': // literal text
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

        rules.add(rule);
    }

    return rules;
}