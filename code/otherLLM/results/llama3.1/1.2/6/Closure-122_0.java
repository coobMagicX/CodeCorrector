private void handleBlockComment(Comment comment) {
  String[] suspiciousKeywords = {"@type", "@typedef", "type:", "typedef:"};
  boolean isSuspicious = false;
  
  for (String keyword : suspiciousKeywords) {
    if (comment.getValue().toLowerCase().contains(keyword)) {
      isSuspicious = true;
      break;
    }
  }

  if (isSuspicious) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}