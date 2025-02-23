import java.util.regex.Pattern;
import java.util.regex.Matcher;

private void handleBlockComment(Comment comment) {
  // Using regex to check for both directly starting block comment annotations
  // and those that might be preceded by a newline and optional spaces or asterisks
  String commentValue = comment.getValue();
  Pattern pattern = Pattern.compile("/\\*\\s*@|\\n\\s*\\*\\s*@");
  Matcher matcher = pattern.matcher(commentValue);

  if (matcher.find()) {
    errorReporter.warning(
        SUSPICIOUS_COMMENT_WARNING,
        sourceName,
        comment.getLineno(), "", 0);
  }
}
