public static Number createNumber(String str) throws NumberFormatException {
    // ...

    int decimalIndex = str.indexOf('.');
    if (decimalIndex != -1) {
        String mantissa = str.substring(0, decimalIndex);
        String exponent = str.substring(decimalIndex + 1);

        boolean allMantissaZeros = true;
        for (char c : mantissa.toCharArray()) {
            if (c != '0') {
                allMantissaZeros = false;
                break;
            }
        }

        // ...

    } else {
        if (str.startsWith("0x") || str.startsWith("-0x")) {
            return createInteger(str);
        }   
        char lastChar = str.charAt(str.length() - 1);
        String mant;
        String dec;
        String exp;
        int expPos = str.indexOf('e') + str.indexOf('E') + 1;

        if (expPos > -1) {
            mant = str.substring(0, expPos);
        } else {
            mant = str;
        }
        dec = null;
        //Requesting a specific type..
        String numeric = str;
        boolean allZeros = isAllZeros(mant) && isAllZeros(str.substring(expPos + 1));
        switch (lastChar) {
            case 'l' :
            case 'L' :
                if (dec == null
                    && exp == null
                    && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                    try {
                        return createLong(numeric);
                    } catch (NumberFormatException nfe) {
                        //Too big for a long
                    }
                    return createBigInteger(numeric);

                }
                throw new NumberFormatException(str + " is not a valid number.");
            case 'f' :
            case 'F' :
                try {
                    Float f = NumberUtils.createFloat(numeric);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                        return f;
                    }

                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                //$FALL-THROUGH$
            case 'd' :
            case 'D' :
                try {
                    Double d = NumberUtils.createDouble(numeric);
                    if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                try {
                    return createBigDecimal(numeric);
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                //$FALL-THROUGH$
            default :
                throw new NumberFormatException(str + " is not a valid number.");

        }
    }
}