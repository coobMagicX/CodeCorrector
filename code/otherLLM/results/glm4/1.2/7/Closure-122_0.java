private void handleBlockComment(Comment comment) {
  String value = comment.getValue();
  int start = value.indexOf("/* @");
  if (start != -1) {
    // Find the end of the block comment to check the full scope
    int end = value.indexOf("*/", start + "/* @".length());
    if (end != -1 && value.substring(start, end).contains("\n * ")) {
      errorReporter.warning(
          SUSPICIOUS_COMMENT_WARNING,
          sourceName,
          comment.getLineno(), "",
          (value.contains("\n") ? value.indexOf("\n", start) : 0));
    }
  } else if (value.indexOf("\n * @") != -1) { // Single line check
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "",
        value.indexOf("\n * @"));
  }
}