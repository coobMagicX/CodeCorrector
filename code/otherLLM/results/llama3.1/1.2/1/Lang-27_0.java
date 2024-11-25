public static Number createNumber(String str) throws NumberFormatException {
    if (str == null) {
        return null;
    }
    if (StringUtils.isBlank(str)) {
        throw new NumberFormatException("A blank string is not a valid number");
    }  
    if (str.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        // it accepts this as a legal value, but it does not appear 
        // to be in specification of class. OS X Java parses it to 
        // a wrong value.
        return null;
    }
    if (str.startsWith("0x") || str.startsWith("-0x")) {
        return createInteger(str);
    }   
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
            exp = str.substring(expPos + 1);
        } else {
            mant = str;
            exp = null;
        }
    }

    // Fix for the bug: handle 'e' and 'E' in scientific notation
    if (exp != null) {
        boolean isScientificNotation = true;
        int ePos = exp.indexOf('e');
        if (ePos > 0) {
            String base = exp.substring(0, ePos);
            String power = exp.substring(ePos + 1);
            try {
                float f = toFloat(base, Float.NaN);
                int p = Integer.parseInt(power);
                if (!Float.isNaN(f)) {
                    return createBigDecimal(mant + "e" + power);
                } else {
                    isScientificNotation = false;
                }
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
        }

        if (isScientificNotation) {
            try {
                Float f = toFloat(mant, Float.NaN);
                if (!Float.isNaN(f)) {
                    return createBigDecimal(str);
                }
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
        }
    }

    if (dec == null && exp == null) {
        //Must be an int,long,bigint
        try {
            return createInteger(mant);
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
        try {
            return createLong(mant);
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
        return createBigInteger(mant);

    } else {
        //Must be a float,double,BigDec
        boolean allZeros = isAllZeros(mant) && exp == null;
        try {
            Float f = toFloat(mant, 0.0F);
            if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                return createBigDecimal(str);
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }
        try {
            Double d = toDouble(mant, 0.0D);
            if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                return createBigDecimal(str);
            }
        } catch (NumberFormatException nfe) {
            // ignore the bad number
        }

        try {
            return createBigDecimal(mant + dec + exp);
        } catch (NumberFormatException e) {
            // ignore the bad number
        }
    }
}