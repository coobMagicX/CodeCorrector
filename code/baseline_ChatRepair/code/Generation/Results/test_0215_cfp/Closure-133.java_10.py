private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine();
  if (result != null && result.contains("\n")) {
    result = result.substring(0, result.indexOf("\n"));
  }
  return result;
}
