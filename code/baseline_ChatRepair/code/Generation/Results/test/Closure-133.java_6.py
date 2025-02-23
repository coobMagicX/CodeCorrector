private String getRemainingJSDocLine() {
  StringBuilder result = new StringBuilder();
  while (stream.hasNext()) {
    String line = stream.getRemainingJSDocLine();
    result.append(line);
    if (!stream.isAtEnd()) {
      result.append("\n");  // Append a newline if not at the end