private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine();
  if (result != null && !result.trim().isEmpty()) {
    // Assuming that JsDocInfoParser can handle newline characters correctly,
    // we can directly trim the result to remove any leading/trailing newlines.
    return result.trim();
  } else {
    // If the result is empty or null, return an empty string
    return "";
  }
}