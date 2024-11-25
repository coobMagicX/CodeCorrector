private void handleBlockComment(Comment comment) {
  if (comment.getValue().indexOf("/* @") != -1 || comment.getValue().indexOf("\n * @") != -1) {
    JSDocInfo jsDocInfo = handleJsdoc(comment.getValue());
    if (jsDocInfo != null) {
      errorReporter.warning(
          SUSPICIOUS_COMMENT_WARNING,
          sourceName,
          comment.getLineno(),
          jsDocInfo.getTagValues(),
          0);
    } else {
      errorReporter.warning(
          SUSPICIOUS_COMMENT_WARNING,
          sourceName,
          comment.getLineno(),
          "",
          0);
    }
  }
}

private JSDocInfo handleJsdoc(String value) {
  // Implementation to parse JSDoc-style comments
}