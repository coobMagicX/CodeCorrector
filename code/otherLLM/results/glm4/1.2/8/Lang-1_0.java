public static Number createNumber(String str) {
    // Remove trailing 'l', 'f', 'd' if present
    str = stripSuffix(str);
    
    // Check for decimal point or exponent part
    String mantissa;
    String exponent = null;
    if (str.contains(".")) {
        int dotIndex = str.indexOf('.');
        mantissa = str.substring(0, dotIndex);
        exponent = str.substring(dotIndex + 1);
    } else {
        mantissa = str;
    }

    // Determine the type of number to create
    if (exponent != null) {
        return new BigDecimal(mantissa + "E" + exponent);
    } else {
        try {
            float f = Float.parseFloat(mantissa);
            if (isPrecisionAcceptable(f)) {
                return f;
            }
        } catch (NumberFormatException e) {}
        
        try {
            double d = Double.parseDouble(mantissa);
            if (isPrecisionAcceptable(d)) {
                return d;
            }
        } catch (NumberFormatException e) {}

        // Default to BigInteger
        return new BigInteger(mantissa);
    }
}

private static boolean isPrecisionAcceptable(float value) {
    return Math.abs(value) < 1e7f; // Example threshold for Float precision
}