function parseNumber(string numStr) {
    if (numStr ends with 'l' or 'L') {
        return createLong(numStr without last character);
    } else if (numStr ends with 'f' or 'F') {
        return createFloat(numStr without last character);
    } else if (numStr ends with 'd' or 'D') {
        return createDouble(numStr without last character);
    } else {
        mantissa = extractMantissa(numStr);
        exponent = extractExponent(numStr, mantissa exists);

        if (no decimal point and no exponent) {
            if (isInteger(mantissa)) {
                return createInteger(mantissa);
            } else {
                return createLong(mantissa) or createBigInteger(mantissa);
            }
        } else {
            numType = decideNumType(numStr, mantissa, exponent);
            switch (numType) {
                case FLOAT:
                    return createFloat(numStr);
                case DOUBLE:
                    return createDouble(numStr);
                default:
                    return createBigDecimal(numStr);
            }
        }
    }
}