private void handleBlockComment(Comment comment) {
    // Check if the comment starts with '/* @' or contains a line starting with ' * @' 
    // Use trim to remove leading and trailing whitespaces before checking
    String trimmedComment = comment.getValue().trim();
    if (trimmedComment.startsWith("/* @") || trimmedComment.contains("\n * @")) {
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}