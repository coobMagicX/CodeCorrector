public static boolean isNumber(String str) {
    if (StringUtils.isEmpty(str)) {
        return false;
    }
    str = str.trim(); // Trims any leading and trailing whitespaces.

    if (str.matches("[+-]?0[xX][0-9a-fA-F]+")) { // Regex for hexadecimal numbers.
        return true;
    }

    try {
        Double.parseDouble(str); // Attempt to parse the string as a Double.
        return true;
    } catch (NumberFormatException e) {
        return false;
    }
}
