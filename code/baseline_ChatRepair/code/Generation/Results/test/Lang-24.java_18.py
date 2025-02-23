public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    // Regex to handle optional sign, digits, optional decimal with digits,
    // and optional exponent component (e.g., 1e-10).
    String numericRegex = "[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?";

    // Hexadecimal regex
    String hexRegex = "[+-]?0[xX][0-9a-fA-F]+";

    str = str.trim(); // Trim the input string.

    if (str.matches(numericRegex) || str.matches(hexRegex)) {
        try {
            // This checks the number doesn't have disallowed trailing characters like 'f', 'd', 'F', 'D'
            // unless it's hexadecimal where such chars might not be positioned as trailing characters of interest.
            if (str.matches("^[fFdDlL]$") && !str.matches("^[xX][0-9a-fA-F]+$")) {
                return false;
            }
            Double.parseDouble(str); // Use Double parsing to confirm valid IEEE format
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    return false;
}
