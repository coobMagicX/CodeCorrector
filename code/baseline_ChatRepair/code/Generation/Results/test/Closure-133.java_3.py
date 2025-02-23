private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine();
  if (result != null) {
    result = result.trim(); // Remove leading and trailing whitespaces
    result = result.replaceFirst("\\s*\\*\\s*$", ""); // Remove trailing