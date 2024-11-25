private String getRemainingJSDocLine() {
  if (stream == null || !stream.hasRemaining()) {
    return "";
  }
  
  try {
    String result = stream.getRemainingJSDocLine();
    if (result.isEmpty()) {
      throw new JSDocParseException("Malformed JSDoc line");
    }
    return result;
  } catch (Exception e) {
    throw new JSDocParseException("Error retrieving JSDoc line", e);
  }
}