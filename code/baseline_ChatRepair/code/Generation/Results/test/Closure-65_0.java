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
      case '\0': 
        sb.append("\\000");  
        break;
      case '\n': 
        sb.append("\\n"); 
        break;
      case '\r': 
        sb.append("\\r"); 
        break;
      case '\t': 
        sb.append("\\t"); 
        break;
      case '\\': 
        sb.append(backslashEscape); 
        break;
      case '\"': 
        sb.append(doublequoteEscape); 
        break;
      case '\'': 
        sb.append(singlequoteEscape); 
        break;
      case '>':
        // To prevent breaking into close comment or breaking dash comment sequence
        if ((i >= 2) && ((s.charAt(i - 1) == '-' && s.charAt(i - 2) == '-') ||
                         (s.charAt(i - 1) == ']' && s.charAt(i - 2) == ']'))) {
          sb.append("\\>");
        } else {
          sb.append(c);
        }
        break;
      case '<':
        // Prevent breaking out of a script block or starting an HTML comment
        if (s.regionMatches(true, i + 1, "/script", 0, "/script".length())) {
          sb.append("<\\");
        } else if (s.regionMatches(false, i + 1, "!--", 0, "!--".length())) {
          sb.append("<\\");
        } else {
          sb.append(c);
        }
        break;
      default:
        // Check for character encoding, escape as necessary
        if (outputCharsetEncoder != null && outputCharsetEncoder.canEncode(c)) {
          sb.append(c);
        } else {
          // Use hex escape for characters that are outside normal ASCII or cannot be encoded
          appendHexJavaScriptRepresentation(sb, c);
        }
    }
  }
  sb.append(quote);
  return sb.toString();
}

private static void appendHexJavaScriptRepresentation(StringBuilder sb, int c) {
    // Represent character in hex indicating it is unicode escaped
    String hex = Integer.toHexString(c);
    sb.append("\\u");
    for (int i = hex.length(); i < 4; i++) {
        sb.append('0');
    }
    sb.append(hex);
}
