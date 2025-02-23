private void handleBlockComment(Comment comment) {
    String value = comment.getValue();
    // Improved regex to capture comment patterns starting after new lines or immediately following block comment start.
    if (value.matches("(?s)\\*\\s*@")) {  // Uses DOTALL mode (?s) to match across lines.
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}
