private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine();
  if (result != null) {
    result = result.replace("\n", " ").trim();
  }
 