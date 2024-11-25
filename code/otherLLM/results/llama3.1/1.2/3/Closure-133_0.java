private String getRemainingJSDocLine() {
  String result = stream.getRemainingJSDocLine();
  
  // Handle newline characters by removing them from the input string
  // before passing it to the stream.getRemainingJSDocLine()
  // method, which should prevent syntax errors due to newline characters.
  result = result.replace("\n", "");
  return result;
}