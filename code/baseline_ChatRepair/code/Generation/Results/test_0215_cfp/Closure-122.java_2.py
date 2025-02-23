private void handleBlockComment(Comment comment) {
    String value = comment.getValue();

    // This regex checks for an annotation start after `/*` which could be just after it or on the next few lines.
    if (value.contains("/*@") || value.contains("\n*@")) {
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}
