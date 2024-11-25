public static Number createNumber(String val) throws NumberFormatException {
    if (val == null) {
        throw new NumberFormatException("null is not a valid number.");
    }
    if (val.isEmpty()) {
        throw new NumberFormatException("\"\" is not a valid number.");
    }
    if (val.startsWith("--")) {
        return null;
    }
    if (val.startsWith("0x") || val.startsWith("-0x")) {
        return createInteger(val);
    }
    char lastChar = val.charAt(val.length() - 1);
    String mantissa;
    String decimalPart;
    String exponentPart;
    int decimalPosition = val.indexOf('.');
    int exponentPosition = val.indexOf('e') + val.indexOf('E') + 1;

    if (decimalPosition > -1) {
        if (exponentPosition > -1) {
            if (exponentPosition < decimalPosition) {
                throw new NumberFormatException(val + " is not a valid number.");
            }
            exponentPart = val.substring(exponentPosition);
        } else {
            exponentPart = null;
        }
        mantissa = val.substring(0, decimalPosition);
        decimalPart = val.substring(decimalPosition + 1);
    } else {
        if (exponentPosition > -1) {
            mantissa = val.substring(0, exponentPosition);
            exponentPart = val.substring(exponentPosition);
        } else {
            mantissa = val;
            decimalPart = null;
        }
    }

    if (!Character.isDigit(lastChar) && lastChar != 'l' && lastChar != 'L' && lastChar != 'f'
        && lastChar != 'F' && lastChar != 'd' && lastChar != 'D') {
        throw new NumberFormatException(val + " is not a valid number.");
    }

    boolean allZeros = isAllZeros(mantissa) && (exponentPart == null || isAllZeros(exponentPart));
    switch (lastChar) {
        case 'l':
        case 'L':
            try {
                return stringToInt(val, 0);
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            break;
        case 'f':
        case 'F':
            try {
                Float f = NumberUtils.createFloat(mantissa + decimalPart);
                if (!(f.isInfinite() || (f.floatValue() == 0.0F && !allZeros))) {
                    return f;
                }
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            break;
        case 'd':
        case 'D':
            try {
                Double d = NumberUtils.createDouble(mantissa + decimalPart);
                if (!(d.isInfinite() || (d.doubleValue() == 0.0D && !allZeros))) {
                    return d;
                }
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            break;
        default:
            try {
                int integer = stringToInt(val, 0);
                if (!allZeros) {
                    return integer;
                }
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            try {
                long longValue = stringToInt(val, 0);
                if (!allZeros) {
                    return longValue;
                }
            } catch (NumberFormatException e) {
                // ignore the bad number
            }
            break;
    }

    throw new NumberFormatException(val + " is not a valid number.");
}

private static boolean isAllZeros(String str) {
    for (int i = 0; i < str.length(); i++) {
        if (!Character.isDigit(str.charAt(i))) {
            return false;
        }
    }
    return true;
}