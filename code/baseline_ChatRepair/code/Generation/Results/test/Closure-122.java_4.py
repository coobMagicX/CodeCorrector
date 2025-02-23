import java.util.regex.Pattern;
import java.util.regex.Matcher;

private void handleBlockComment(Comment comment) {
    String commentValue = comment.getValue();
    // Using regex to detect if it contains the pattern starting with *
    // followed by any number of spaces and an '@' symbol
    Pattern pattern = Pattern.compile("\\*\\s+@");
    Matcher matcher = pattern.matcher(commentValue);

    // Check if the pattern is found in the comment
    if (matcher.find()) {
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}
