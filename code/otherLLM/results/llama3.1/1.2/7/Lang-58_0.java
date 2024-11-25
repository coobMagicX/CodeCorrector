public static Number createNumber(String str) throws NumberFormatException {
    // ... (rest of the code remains the same)

    switch (lastChar) {
        case 'l' :
        case 'L' :
            if (dec == null
                && exp == null
                && isDigits(numeric.substring(1))
                && (numeric.charAt(0) == '-' || Character.isDigit(numeric.charAt(0)))) {
                try {
                    return createLong(str); // Use the str argument directly instead of numeric
                } catch (NumberFormatException nfe) {
                    //Too big for a long
                }
                return createBigInteger(str);
            }
            throw new NumberFormatException(str + " is not a valid number.");
        case 'f' :
        case 'F' :
            try {
                Float f = NumberUtils.createFloat(numeric);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    //If it's too big for a float or the float value = 0 and the string
                    //has non-zeros in it, then float does not have the precision we want
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
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
            //Fall through
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
            try {
                Float f = NumberUtils.createFloat(numeric);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return createBigDecimal(numeric);
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            //Fall through

        default :
            throw new NumberFormatException(str + " is not a valid number.");
    }
}