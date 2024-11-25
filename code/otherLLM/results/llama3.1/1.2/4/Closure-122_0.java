private void handleBlockComment(Comment comment) {
  if (comment.getValue().indexOf("/* @") != -1 || comment.getValue().indexOf("\n * @") != -1 
      || comment.getValue().matches(".*(/*\\s*@.*|@.*).*")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
      comment.getLineno(), "", 0);
  }
}