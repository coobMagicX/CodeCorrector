private void handleBlockComment(Comment comment) {
    String value = comment.getValue();
    if (value.indexOf("/* @") != -1 || value.indexOf("\n * @") != -1 ||
        value.indexOf(" /* @") != -1 || value.indexOf("\n  * @") != -1) { // Added extra spaces to cover leading whitespace
      errorReporter.warning(
          SUSPICIOUS_COMMENT_WARNING,
          sourceName,
          comment.getLineno(), "", 0);
    }
}