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
            if (tokenLen == 7 && isPatternSpecific(token)) { // NEW CONDITION
                rule = new TextField(Calendar.MONTH, getCustomMonthFormat());
            } else if (tokenLen >= 4) {
                rule = new TextField(Calendar.MONTH, months);
            } else if (tokenLen == 3) {
                rule = new TextField(Calendar.MONTH, shortMonths);
            } else if (tokenLen == 2) {
                rule = TwoDigitMonthField.INSTANCE;
            } else {
                rule = UnpaddedMonthField.INSTANCE;
            }
            break;
        case 'd': // day in month (number)
            rule = selectNumberRule(Calendar.DAY_OF_MONTH, tokenLen);
            break;
        // ... rest of the code remains the same ...