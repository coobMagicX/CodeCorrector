public static Integer createInteger(String numeric) {
    try {
        return Integer.parseInt(numeric);
    } catch (NumberFormatException nfe) {
        throw new NumberFormatException("Invalid integer: " + numeric);
    }
}

public static Long createLong(String numeric) {
    try {
        return Long.parseLong(numeric);
    } catch (NumberFormatException nfe) {
        throw new NumberFormatException("Invalid long: " + numeric);
    }
}

public static BigInteger createBigInteger(String numeric) {
    try {
        return new BigInteger(numeric);
    } catch (NumberFormatException nfe) {
        throw new NumberFormatException("Invalid big integer: " + numeric);
    }
}

public static Float createFloat(String numeric) {
    try {
        return Float.parseFloat(numeric);
    } catch (NumberFormatException nfe) {
        throw new NumberFormatException("Invalid float: " + numeric);
    }
}

public static Double createDouble(String numeric) {
    try {
        return Double.parseDouble(numeric);
    } catch (NumberFormatException nfe) {
        throw new NumberFormatException("Invalid double: " + numeric);
    }
}

public static BigDecimal createBigDecimal(String numeric) {
    try {
        return new BigDecimal(numeric);
    } catch (NumberFormatException nfe) {
        throw new NumberFormatException("Invalid big decimal: " + numeric);
    }
}