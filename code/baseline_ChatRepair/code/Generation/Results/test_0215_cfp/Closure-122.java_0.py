private void handleBlockComment(Comment comment) {
  String trimmedValue = comment.getValue().trim();
  if (trimmedValue.matches("\\/\\*\\s*\\*?\\s*@")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}
