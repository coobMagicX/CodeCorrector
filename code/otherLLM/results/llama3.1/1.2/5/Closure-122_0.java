private void handleBlockComment(Comment comment) {
  int startPosition = position2charno(comment.getLineno());
  String value = comment.getValue();
  
  if (value.indexOf("/* @", startPosition) != -1 || 
      (value.indexOf("\n") != -1 && value.substring(value.indexOf("\n") + 1).indexOf(" * @") != -1)) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}