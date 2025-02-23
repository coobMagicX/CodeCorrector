private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine().trim();
  return result.replaceAll("\\s+\\*\\s*$", ""); // Remove trailing aster