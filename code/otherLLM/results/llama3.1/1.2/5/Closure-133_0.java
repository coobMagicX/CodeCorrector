private String getRemainingJSDocLine() {
  JsDocToken token = next();
  
  // Input validation for the parse method
  if (token == null || !token.getType().equals(JsDocTokenType.JSDOC_LINE)) {
    return null; // or throw an exception, depending on the desired behavior
  }
  
  String result = stream.getRemainingJSDocLine(token);
  return result;
}