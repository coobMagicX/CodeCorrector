private void handleBlockComment(Comment comment) {
    // Use a regular expression to find lines starting with any amount of whitespace followed by "* @"
    Pattern pattern = Pattern.compile("\\n\\s*\\*\\s*@");
    Matcher matcher = pattern.matcher(comment.getValue());
    if (matcher.find()) {
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}