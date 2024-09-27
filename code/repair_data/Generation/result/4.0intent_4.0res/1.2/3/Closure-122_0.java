private void handleBlockComment(Comment comment) {
    String commentValue = comment.getValue();
    Pattern pattern = Pattern.compile("\\n\\s*\\*\\s*@", Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(commentValue);
    if (matcher.find()) {
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}