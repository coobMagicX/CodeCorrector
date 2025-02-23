private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine();
  if (result != null) {
    result = result.replaceAll("[\\n\\r]+", " ").trim();
  }
  return result;
}
