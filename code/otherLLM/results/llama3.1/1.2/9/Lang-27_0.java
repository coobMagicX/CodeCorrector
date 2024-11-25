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
        } else {
            mant = str;
        }
        dec = null;
    }
    if (!Character.isDigit(lastChar) && lastChar != '.') {
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length() - 1);
        } else {
            exp = null;
        }
        //Requesting a specific type..
        String numeric = str.substring(0, str.length() - 1);
        boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
        switch (lastChar) {
            case 'l' :
            case 'L' :
                if (dec == null
                    && exp == null
                    && (str.equalsIgnoreCase("Infinity") || str.equalsIgnoreCase("-Infinity"))) {
                    return null;
                }
                if (dec == null && exp == null) {
                    //Must be an int,long,bigint
                    try {
                        return createInteger(str);
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    try {
                        return createLong(str);
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    return createBigInteger(str);

                } else {
                    //Must be a float,double,BigDec
                    boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
                    try {
                        Float f = createFloat(str);
                        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                            return f;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    try {
                        Double d = createDouble(str);
                        if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                            return d;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }

                    return createBigDecimal(str);

                }
            case 'd' :
            case 'D' :
                if (dec != null || exp != null) {
                    try {
                        Double d = toFloat(str, 0.0);
                        if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                            return d;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                try {
                    return createDouble(numeric);
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                try {
                    return createBigDecimal(str);
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                //$FALL-THROUGH$
            case 'f' :
                try {
                    Float f = toFloat(str, 0.0);
                    if (!(f.isInfinite() || (f.floatValue() == 0.0F))) {
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                try {
                    return createFloat(numeric);
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                try {
                    return createBigDecimal(str);
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }
                //$FALL-THROUGH$
            case 'g' :
            default :
                if (dec != null || exp != null) {
                    try {
                        Double d = toFloat(str, 0.0);
                        if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                            return d;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                try {
                    return createBigDecimal(numeric);
                } catch (NumberFormatException e) {
                    // ignore the bad number
                }

        }
    } else {
        //User doesn't have a preference on the return type, so let's start
        //small and go from there...
        if (expPos > -1 && expPos < str.length() - 1) {
            exp = str.substring(expPos + 1, str.length());
        } else {
            exp = null;
        }
        if (dec == null && exp == null) {
            //Must be an int,long,bigint
            try {
                return createInteger(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                return createLong(str);
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            return createBigInteger(str);

        } else {
            //Must be a float,double,BigDec
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            try {
                Float f = toFloat(str, 0.0);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }
            try {
                Double d = toDouble(str, 0.0);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // ignore the bad number
            }

            return createBigDecimal(str);

        }
    }
}