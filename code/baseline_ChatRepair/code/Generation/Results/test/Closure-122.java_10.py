private void handleBlockComment(Comment comment) {
  String commentValue = comment.getValue();
  // Regular Expression to search for suspicious comments with possible spaces before '*'
  if (commentValue.contains("/* @") || commentValue.contains("\n * @")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}
