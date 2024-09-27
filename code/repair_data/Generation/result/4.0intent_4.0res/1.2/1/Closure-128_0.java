static String minifyJavaScriptObject(String jsObject) {
    StringBuilder sb = new StringBuilder();
    int len = jsObject.length();
    boolean inQuotes = false;
    char currentChar;

    for (int index = 0; index < len; index++) {
        currentChar = jsObject.charAt(index);
        if (currentChar == '\"') {
            inQuotes = !inQuotes;
        }
        if (!inQuotes && (currentChar == ' ' || currentChar == '\n' || currentChar == '\t')) {
            continue;
        }
        sb.append(currentChar);
    }

    return sb.toString();
}