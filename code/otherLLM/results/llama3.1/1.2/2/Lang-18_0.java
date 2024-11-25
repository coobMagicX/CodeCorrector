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
            if (tokenLen >= 4) {
                rule = new TextField(Calendar.MONTH, months);
            } else if (tokenLen == 3) {
                rule = new TextField(Calendar.MONTH, shortMonths);
            } else if (tokenLen == 2) {
                rule = TwoDigitMonthField.INSTANCE;
            } else {
                rule = UnpaddedMonthField.INSTANCE;
            }
            break;
        case 'd': // day of month (number)
            if (tokenLen >= 4) {
                rule = selectNumberRule(Calendar.DAY_OF_MONTH, tokenLen);
            } else {
                rule = SingleDigitDayOfMonthField.INSTANCE;
            }
            break;
        case 'a': // short time (text)
            rule = new ShortTimeNameRule(mTimeZone, mLocale);
            break;
        case 'A': // long time (text)
            rule = new LongTimeNameRule(mTimeZone, mLocale);
            break;
        case 'z': // time zone (text)
            if (tokenLen >= 4) {
                rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.LONG);
            } else {
                rule = new TimeZoneNameRule(mTimeZone, mLocale, TimeZone.SHORT);
            }
            break;
        case 'Z': // time zone (value)
            if (tokenLen == 1) {
                rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
            } else {
                rule = TimeZoneNumberRule.INSTANCE_COLON;
            }
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

class ShortTimeNameRule extends TimeZoneNameRule {
    public ShortTimeNameRule(TimeZone timeZone, Locale locale) {
        super(timeZone, locale, TimeZone.SHORT);
    }
}

class LongTimeNameRule extends TimeZoneNameRule {
    public LongTimeNameRule(TimeZone timeZone, Locale locale) {
        super(timeZone, locale, TimeZone.LONG);
    }
}