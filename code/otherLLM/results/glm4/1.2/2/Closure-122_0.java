private void handleBlockComment(Comment comment) {
  // Use a regex pattern to check for the suspicious comments starting with "/* @" or "\n *@"
  // The regex pattern also allows for any characters after the opening sequence including whitespace
  if (comment.getValue().matches(".*\\* @(.*))")) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "",
        0);
  }
}