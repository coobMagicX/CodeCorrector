public static Number createNumber(String str) throws NumberFormatException {
    // ...
    char lastChar = str.charAt(str.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = str.indexOf('.');
    int expPos = str.indexOf('e') + str.indexOf('E') + 1;

    if (decPos > -1) {

        if (expPos > -1) {
            if (expPos < decPos) {
                throw new NumberFormatException(str + " is not a valid number.");
            }
            dec = str.substring(decPos + 1, expPos);
        } else {
            dec = str.substring(decPos + 1);
        }
        mant = str.substring(0, decPos);
    } else {
        if (expPos > -1) {
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
    }

    // Additional check for "l" suffix
    if (lastChar == 'l' || lastChar == 'L') {
        String numeric = str.substring(0, str.length() - 1);
        boolean allZeros = isAllZeros(mant) && isAllZeros(dec);
        if (dec == null && exp == null && isDigits(numeric.substring(1)) && (numeric.charAt(0) == '-' || Character.isDigit(numeric.charAt(0)))) {
            try {
                return createLong(numeric);
            } catch (NumberFormatException nfe) {
                //Too big for a long
            }
            return createBigInteger(numeric);
        }
    }

    if (!Character.isDigit(lastChar)) {
        // ...