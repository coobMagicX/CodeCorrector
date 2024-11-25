private String getRemainingJSDocLine() {
  try {
    String result = stream.getRemainingJSDocLine();
    if (result == null) {
      skipEOLs(); // This might be necessary to handle malformed JSDoc annotations
      return "";
    }
    return result;
  } catch (Exception e) {
    throw new RuntimeException("Error parsing JSDoc annotation: " + e.getMessage());
  }
}