private void handleBlockComment(Comment comment) {
  String commentValue = comment.getValue();
  // Use string matching to detect misplaced annotations with "/* @" immediately or with new line and space(s).
  if (commentValue.contains("/* @") || commentValue.contains("\n * @")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}
