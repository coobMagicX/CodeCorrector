public static Number createNumber(String val) throws NumberFormatException {
    if (val == null) {
        return null;
    }
    if (val.length() == 0) {
        throw new NumberFormatException("\"\" is not a valid number.");
    }
    if (val.startsWith("--")) {
        // this is protection for poorness in java.lang.BigDecimal.
        // it accepts this as a legal value, but it does not appear 
        // to be in specification of class. OS X Java parses it to 
        // a wrong value.
        return null;
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        return createInteger(val);
    }

    char lastChar = val.charAt(val.length() - 1);
    String mant;
    String dec;
    String exp;
    int decPos = val.indexOf('.');
    int expPos = val.indexOf('e') + val.indexOf('E') + 1;

    if (decPos > -1) {
        if (expPos > -1) {
            if (expPos < decPos) {
                throw new NumberFormatException(val + " is not a valid number.");
            }
            dec = val.substring(decPos + 1, expPos);
        } else {
            dec = val.substring(decPos + 1);
        }
        mant = val.substring(0, decPos);
    } else {
        if (expPos > -1) {
            mant = val.substring(0, expPos);
        } else {
            mant = val;
        }
        dec = null;
    }

    if (!Character.isDigit(lastChar)) {
        //Requesting a specific type..
        String numeric = val.substring(0, val.length() - 1);
        boolean allZeros = isAllZeros(mant) && (dec == null || isAllZeros(dec));
        switch (lastChar) {
            case 'l':
            case 'L':
                if (dec == null && exp == null
                    && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                    try {
                        return createLong(numeric);
                    } catch (NumberFormatException nfe) {
                        //Too big for a long
                    }
                    return createBigInteger(numeric);

                }
                throw new NumberFormatException(val + " is not a valid number.");
            case 'f':
            case 'F':
                if (dec == null && exp == null) {
                    try {
                        Float f = NumberUtils.createFloat(numeric);
                        if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                            //If it's too big for a float or the float value = 0 and the string
                            //has non-zeros in it, then float does not have the precision we want
                            return f;
                        }
                    } catch (NumberFormatException e) {
                        // ignore the bad number
                    }
                }
                break;
            case 'd':
            case 'D':
                if (dec == null && exp == null) {
                    try {
                        Double d = NumberUtils.createDouble(numeric);
                        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                            return d;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                }
                break;
            default:
                throw new NumberFormatException(val + " is not a valid number.");
        }

    } else {
        if (dec == null && exp == null) {
            try {
                return createInteger(val);
            } catch (NumberFormatException nfe) {
                // empty catch
            }
            try {
                return createLong(val);
            } catch (NumberFormatException nfe) {
                // empty catch
            }
            return createBigInteger(val);

        } else {
            boolean allZeros = isAllZeros(mant) && (dec == null || isAllZeros(dec));
            try {
                Float f = createFloat(val);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException nfe) {
                // empty catch
            }
            try {
                Double d = createDouble(val);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException nfe) {
                // empty catch
            }

            return createBigDecimal(val);

        }
    }
}

private static boolean isAllZeros(String str) {
    for (char c : str.toCharArray()) {
        if (c != '0') {
            return false;
        }
    }
    return true;
}