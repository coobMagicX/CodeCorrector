private void handleBlockComment(Comment comment) {
  String commentValue = comment.getValue();
  if (commentValue.indexOf("/* @") != -1 || commentValue.matches("\n\\s*\\*@")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}
