private String getRemainingJSDocLine() {
    StringBuilder result = new StringBuilder();
    String line = stream.getRemainingJSDocLine();
    while (line != null && !line.endsWith("*/")) {
        result.append(line);
        line = stream.getRemainingJSDocLine();
    }
    if (line != null) {
        result.append(line.substring(0, line.indexOf("*/")));
    }
    return result.toString();
}