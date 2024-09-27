private void handleBlockComment(Comment comment) {
    String commentValue = comment.getValue();
    String[] lines = commentValue.split("\\r?\\n");
    boolean suspiciousLineFound = false;
    for (String line : lines) {
        if (line.trim().startsWith("* @")) {
            suspiciousLineFound = true;
            break;
        }
    }
    if (suspiciousLineFound) {
        errorReporter.warning(
                SUSPICIOUS_COMMENT_WARNING,
                sourceName,
                comment.getLineno(), "", 0);
    }
}