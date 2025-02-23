private void handleBlockComment(Comment comment) {
    // Use regex to match the suspicious comment pattern
    String commentValue = comment.getValue();
    Pattern pattern = Pattern.compile("\\*\\s*@");
    Matcher matcher = pattern.matcher(commentValue);

    if (matcher.find()) {
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}
