import java.util.regex.Pattern;
import java.util.regex.Matcher;

private void handleBlockComment(Comment comment) {
    // Regex to detect both the start of a comment line just after `/*` and subsequent lines starting with `*`
    Pattern pattern = Pattern.compile("/\\*\\s*@|\\n\\s*\\*\\s*@");
    Matcher matcher = pattern.matcher(comment.getValue());
    
    if (matcher.find()) {
        errorReporter.warning(
            SUSPICIOUS_COMMENT_WARNING,
            sourceName,
            comment.getLineno(), "", 0);
    }
}
