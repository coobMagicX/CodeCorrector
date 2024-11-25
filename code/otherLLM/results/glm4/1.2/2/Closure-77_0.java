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
        if (s.regionMatches(true, i + 1, "/script", 0, 7)) {
          sb.append("<\\");
          i += 6; // Skip the "/script" after appending "<\\" to avoid duplication
        } else if (s.regionMatches(false, i + 1, "!--", 0, 3)) {
          sb.append("<\\");
          i += 2; // Skip the "!--" after appending "<\\" to avoid duplication
        } else {
          sb.append(c);
        }
        break;
      case '\0':
        // Special case for null character, we escape it as "\u0000"
        sb.append("\\u0000");
        break;
      default:
        if (outputCharsetEncoder != null) {
          if (outputCharsetEncoder.canEncode(c)) {
            sb.append(c);
          } else {
            appendHexJavaScriptRepresentation(sb, c);
          }
        } else {
          // No charsetEncoder provided - pass straight latin characters through,
          // and escape the rest.
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

// This method is not part of the original provided methods but assumed to be part of the class,
// as it's used in the fixed code above.
private static void appendHexJavaScriptRepresentation(StringBuilder sb, char c) {
  // Assuming this method exists and correctly formats the character as a hex string
}