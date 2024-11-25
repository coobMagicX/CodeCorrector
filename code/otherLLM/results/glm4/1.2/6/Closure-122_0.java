private void handleBlockComment(Comment comment) {
    if (comment.getValue().contains("/* @") || comment.getValue().contains("\n * @")) {
        // Improved detection logic to account for possible line breaks and spaces in the suspicious pattern
        String[] patterns = new String[]{"/*@", "\n *@"};
        boolean isSuspicious = false;
        
        for (String pattern : patterns) {
            if (comment.getValue().contains(pattern)) {
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
}