private void handleBlockComment(Comment comment) {
  // Updated to use regular expression matching to account for varying whitespaces
  String commentValue = comment.getValue();
  Pattern pattern = Pattern.compile("\\n\\s*\\*@");
  Matcher matcher = pattern.matcher(commentValue