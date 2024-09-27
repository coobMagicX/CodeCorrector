static String stripLeadingAndTrailingQuotes(String str) {
    while (str.startsWith("\"") && str.endsWith("\"")) {
        str = str.substring(1, str.length() - 1);
    }
    
    return str;
}