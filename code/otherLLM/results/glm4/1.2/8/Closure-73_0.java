static String strEscape(String s, char quote,
                        String doublequoteEscape,
                        String singlequoteEscape,
                        String backslashEscape,
                        CharsetEncoder outputCharsetEncoder) {
  StringBuilder sb = new StringBuilder(s.length() + 2);
  sb.append(quote);
  for (int i = 0; i < s.length(); i++) {
    char c = s.charAt(i);
    switch (c) {
      case '\0': sb.append("\\0"); break;
      case '\n': sb.append("\\n"); break;
      case '\r': sb.append("\\r"); break;
      case '\t': sb.append("\\t"); break;
      case '\\': sb.append(backslashEscape); break;
      case '\"': sb.append(doublequoteEscape); break;
      case '\'': sb.append(singlequoteEscape); break;
      case '>':
        if (i >= 2 &&
            ((s.charAt(i - 1) == '-' && s.charAt(i - 2) == '-') ||
             (s.charAt(i - 1) == ']' && s.charAt(i - 2) == ']'))) {
          sb.append("\\>");
        } else {
          sb.append(c);
        }
        break;
      case '<':
        final String END_SCRIPT = "/script";
        final String START_COMMENT = "!--";

        if (s.regionMatches(true, i + 1, END_SCRIPT, 0,
                            END_SCRIPT.length())) {
          sb.append("<\\");
        } else if (s.regionMatches(false, i + 1, START_COMMENT, 0,
                                   START COMMENT.length())) { // Fixed missing space in the string
          sb.append("<\\");
        } else {
          sb.append(c);
        }
        break;
      default:
        if (outputCharsetEncoder != null) {
          if (!outputCharsetEncoder.canEncode(c)) {
            appendHexJavaScriptRepresentation(sb, c);
          } else {
            sb.append(c);
          }
        } else {
          if (c > 0x1f && c <= 0x7f) {
            sb.append(c);
          } else {
            appendHexJavaScriptRepresentation(sb, c);
          }
        }
    }
  }
  sb.append(quote);
  return sb.toString();
}

private static void appendHexJavaScriptRepresentation(
      int codePoint, Appendable out)
      throws IOException {
    if (Character.isSupplementaryCodePoint(codePoint)) {
      char[] surrogates = Character.toChars(codePoint);
      appendHexJavaScriptRepresentation(surrogates[0], out);
      appendHexJavaScriptRepresentation(surrogates[1], out);
      return;
    }
    out.append("\\u")
        .append(HEX_CHARS[(codePoint >>> 12) & 0xf])
        .append(HEX_CHARS[(codePoint >>> 8) & 0xf])
        .append(HEX_CHARS[(codePoint >>> 4) & 0xf])
        .append(HEX_CHARS[codePoint & 0xf]);
}

// Assuming HEX_CHARS is defined elsewhere in the code
static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7',
                                  '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};