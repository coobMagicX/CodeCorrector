public List<Rule> parsePattern() {
    // existing code...

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
            // existing code...

            case 'Y': // year in four digits (number)
                rule = selectNumberRule(Calendar.YEAR, tokenLen);
                break;

            default:
                throw new IllegalArgumentException("Illegal pattern component: " + token);
        }

        rules.add(rule);
    }

    return rules;
}

public Rule selectNumberRule(int field, int length) {
    if (length >= 4) {
        return new TextField(field, Arrays.copyOfRange(mPattern.toCharArray(), indexRef[0], indexRef[0] + length));
    } else {
        return TwoDigitField.INSTANCE;
    }
}