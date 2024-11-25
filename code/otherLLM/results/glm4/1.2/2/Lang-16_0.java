public Number createNumber(String numberString) {
    int lastCharIndex = numberString.length() - 1;
    int expPos = Math.max(numberString.indexOf('e'), numberString.indexOf('E')) + 1;
    int decPos = numberString.indexOf('.');

    // Determine the type of the number based on suffix and content
    if (Character.isDigit(numberString.charAt(lastCharIndex))) {
        // No suffix, check for exponent or decimal point
        if (decPos > -1) {
            return parseDecimalOrExponential(numberString, decPos);
        } else {
            return Long.parseLong(numberString);  // Assume it's a long
        }
    } else {
        switch (numberString.charAt(lastCharIndex)) {
            case 'l':
            case 'L':
                return Long.parseLong(numberString.substring(0, lastCharIndex));
            case 'f':
            case 'F':
                if (decPos > -1) {
                    return parseDecimalOrExponential(numberString, decPos);
                } else {
                    return Float.parseFloat(numberString);  // Assume it's a float
                }
            case 'd':
            case 'D':
                if (decPos > -1 || expPos > -1) {
                    return Double.parseDouble(numberString);
                } else {
                    return Double.valueOf(numberString);  // Assume it's a double
                }
            default:
                throw new IllegalArgumentException("Invalid number suffix: " + numberString.charAt(lastCharIndex));
        }
    }
}

private Number parseDecimalOrExponential(String numberString, int decimalPos) {
    String mantissa = numberString.substring(0, decimalPos);
    String fraction = (decimalPos < numberString.length() - 1) ? numberString.substring(decimalPos + 1) : null;
    if (fraction == null || expPos > -1 && expPos <= decimalPos) {
        return Double.parseDouble(mantissa + "0" + (expPos > -1 ? "E" + fraction : ""));
    } else {
        return Double.parseDouble(mantissa);
    }
}