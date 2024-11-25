private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

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
                                   START COMMENT.length())) {
          sb.append("<\\");
        } else {
          sb.append(c);
        }
        break;
      default:
        if ((outputCharsetEncoder == null && c > 0x7f) ||
            (!Character.isISOControl(c))) {
          appendHexJavaScriptRepresentation(sb, c);
        } else {
          sb.append(c);
        }
    }
  }
  sb.append(quote);
  return sb.toString();
}

private static void appendHexJavaScriptRepresentation(StringBuilder sb, char c) {
  if (c < 0x10000) { // BMP
    sb.append("\\u");
    sb.append(toHex(c));
  } else { // supplementary planes
    int codePoint = Character.codePointAt(Character.toChars(c), 1);
    sb.append("\\U");
    sb.append(toHex(codePoint, 8));
  }
}

private static String toHex(int value, int digitCount) {
  StringBuilder hexString = new StringBuilder(digitCount);
  for (int i = 0; i < digitCount - 1; i++) {
    hexString.append("0");
  }
  hexString.append(Integer.toHexString(value));
  return hexString.toString();
}

private static String toHex(int value) {
  return Integer.toHexString(value).toUpperCase();
}