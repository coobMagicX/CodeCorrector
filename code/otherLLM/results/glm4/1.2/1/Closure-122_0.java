private void handleBlockComment(Comment comment) {
  String value = comment.getValue();
  if (value.contains("/* @") || value.contains("\n * @")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}