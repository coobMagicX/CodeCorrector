protected List<Rule> parsePattern() {
    DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
    List<Rule> rules = new ArrayList<>();

    String[] ERAs = symbols.getEras();
    String[] months = symbols.getMonths();
    String[] shortMonths = symbols.getShortMonths();
    String[] weekdays = symbols.getWeekdays();
    String[] shortWeekdays = symbols.getShortWeekdays();
    String[] AmPmStrings = symbols.getAmPmStrings();

    int length = mPattern.length();
    int[] indexRef = {0}; // Use an array to allow for the use of 'indexRef[0]' in the lambda expressions

    for (int i = 0; i < length; i++) {
        String token = parseToken(mPattern, index -> index[0] = i);
        if (token.length() == 0) {
            break;
        }

        Rule rule;
        char c = token.charAt(0);

        switch (c) {
        case 'G':
            rule = new TextField(Calendar.ERA, ERAs);
            break;
        case 'y':
            rule = selectNumberRule(Calendar.YEAR, token.length());
            if (rule == null) rule = TwoDigitYearField.INSTANCE; // Ensure the rule is not null
            break;
        case 'M':
            rule = selectNumberRule(Calendar.MONTH, token.length());
            if (rule == null) {
                if (token.length() == 3) {
                    rule = new TextField(Calendar.MONTH, shortMonths);
                } else if (token.length() == 2) {
                    rule = TwoDigitMonthField.INSTANCE;
                } else {
                    rule = UnpaddedMonthField.INSTANCE;
                }
            }
            break;
        case 'd':
            rule = selectNumberRule(Calendar.DAY_OF_MONTH, token.length());
            break;
        case 'h':
            rule = new TwelveHourField(selectNumberRule(Calendar.HOUR, token.length()));
            break;
        case 'H':
            rule = selectNumberRule(Calendar.HOUR_OF_DAY, token.length());
            break;
        case 'm':
            rule = selectNumberRule(Calendar.MINUTE, token.length());
            break;
        case 's':
            rule = selectNumberRule(Calendar.SECOND, token.length());
            break;
        case 'S':
            rule = selectNumberRule(Calendar.MILLISECOND, token.length());
            break;
        case 'E':
            rule = new TextField(Calendar.DAY_OF_WEEK, token.length() < 4 ? shortWeekdays : weekdays);
            break;
        case 'D':
            rule = selectNumberRule(Calendar.DAY_OF_YEAR, token.length());
            break;
        case 'F':
            rule = selectNumberRule(Calendar.DAY_OF_WEEK_IN_MONTH, token.length());
            break;
        case 'w':
            rule = selectNumberRule(Calendar.WEEK_OF_YEAR, token.length());
            break;
        case 'W':
            rule = selectNumberRule(Calendar.WEEK_OF_MONTH, token.length());
            break;
        case 'a':
            rule = new TextField(Calendar.AM_PM, AmPmStrings);
            break;
        case 'k':
            rule = new TwentyFourHourField(selectNumberRule(Calendar.HOUR_OF_DAY, token.length()));
            break;
        case 'K':
            rule = selectNumberRule(Calendar.HOUR, token.length());
            break;
        case 'z':
            if (token.length() >= 4) {
                rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.LONG);
            } else {
                rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.SHORT);
            }
            break;
        case 'Z':
            if (token.length() == 1) {
                rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
            } else {
                rule = TimeZoneNumberRule.INSTANCE_COLON;
            }
            break;
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

        rules.add(rule); // Add the rule to the list
    }

    return rules; // Return the completed rules list
}