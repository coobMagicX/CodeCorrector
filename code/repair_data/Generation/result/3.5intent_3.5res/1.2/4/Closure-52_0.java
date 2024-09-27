static boolean isSimpleNumber(String s) {
  // Check if the string matches any of the acceptable numeric formats
  if (s.matches("^[0-9]+$") || s.matches("^0[xX][0-9a-fA-F]+$") || s.matches("^0[0-7]+$") || s.matches("^[-+]?[0-9]*\\.?[0-9]*$")) {
    return true;
  }
  
  return false;
}

private static void appendHexJavaScriptRepresentation(int codePoint, Appendable out) throws IOException {
  if (Character.isSupplementaryCodePoint(codePoint)) {
    // Handle supplementary unicode values which are not representable in javascript
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