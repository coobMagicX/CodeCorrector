static String stripLeadingHyphens(String str) {
    if (str == null) {
        return null; // or throw an exception, depending on the desired behavior
    }

    if (str.startsWith("--")) {
        return str.substring(2, str.length());
    } else if (str.startsWith("-")) {
        return str.substring(1, str.length());
    }

    return str;
}