private String getRemainingJSDocLine() {
  while (true) {
    String result = stream.getRemainingJSDocLine();
    if (!match(JsDocToken.END_OF_LINE_COMMENT, JsDocToken.START_OF_BLOCK_TAG)) {
      return result; // If not a comment or block tag, return the line
    }
    stream.unreadNext(); // Unread the token to allow parsing of subsequent lines
  }
}