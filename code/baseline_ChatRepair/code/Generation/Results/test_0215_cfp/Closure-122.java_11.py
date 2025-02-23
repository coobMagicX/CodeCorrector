private void handleBlockComment(Comment comment) {
  String commentValue = comment.getValue();
  // Regular expression to catch '/*@' following any number of spaces after '/*' or on a newline following any number of spaces and '*'
  if (Pattern.compile("(/\\*\\s*@)|(\\n\\s*\\*\\s*@)").matcher(commentValue).find()) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}
