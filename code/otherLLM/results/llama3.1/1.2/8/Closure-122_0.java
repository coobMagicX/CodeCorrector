private void handleBlockComment(Comment comment) {
  if (comment.getValue().matches(".*/* @.*|.*\\n * @.*")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}