private String getRemainingJSDocLine() {
    StringBuilder result = new StringBuilder();
    String line = stream.getRemainingJSDocLine();
    result.append(line);
    while (line.trim().endsWith("*")) {
        line = stream.getRemainingJSDocLine();
        if (!line.trim().startsWith("*")) {
            break;
        }
        result.append("\n").append(line);
    }
    return result.toString();
}