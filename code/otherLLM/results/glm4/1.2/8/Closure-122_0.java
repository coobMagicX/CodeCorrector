private void handleBlockComment(Comment comment) {
  if (comment.getValue().contains("/* @") || comment.getValue().contains("\n * @")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}