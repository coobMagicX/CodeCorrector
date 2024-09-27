static String stripLeadingAndTrailingQuotes(String str) {
    str = str.replaceAll("^\"|\"$", "");
    return str;
}