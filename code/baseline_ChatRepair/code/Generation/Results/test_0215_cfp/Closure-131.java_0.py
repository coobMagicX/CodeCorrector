public static boolean isJSIdentifier(String s) {
  if (s == null || s.isEmpty()) return false;
  // This regular expression will check if the start of the string conforms to JS identifier start
  // characters and if the rest of the string consists of valid JS identifier parts
  // \\p{L} matches any kind of letter from any language
 