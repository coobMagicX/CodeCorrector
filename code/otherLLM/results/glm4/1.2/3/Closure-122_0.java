private void handleBlockComment(Comment comment) {
  // Split the comment value into lines to check for suspicious patterns across different lines
  List<String> lines = Arrays.asList(comment.getValue().split("\n"));
  
  // Iterate over each line to find a suspicious pattern at the beginning of any line
  for (String line : lines) {
    if (line.startsWith("/* @") || line.startsWith("* @")) {
      errorReporter.warning(
          SUSPICIOUS_COMMENT_WARNING,
          sourceName,
          comment.getLineno(), "", 0);
      break; // No need to check further once a suspicious pattern is found
    }
  }
}