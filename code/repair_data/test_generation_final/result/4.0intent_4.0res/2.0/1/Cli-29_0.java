static String stripLeadingAndTrailingQuotes(String str) {
    if (str != null && str.startsWith("\"") && str.endsWith("\"") && str.length() > 1) {
        str = str.substring(1, str.length() - 1);
    }
    return str;
}