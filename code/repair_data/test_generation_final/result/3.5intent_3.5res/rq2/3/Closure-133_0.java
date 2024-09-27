private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine().replaceAll("\\{@[a-zA-Z]+\\s[a-zA-Z]+\\}", "");
  return result.trim();
}