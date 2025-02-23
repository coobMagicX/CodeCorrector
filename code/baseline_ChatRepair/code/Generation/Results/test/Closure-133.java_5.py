private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine();
  if (result.contains("\n")) {
    // Adjust if there is