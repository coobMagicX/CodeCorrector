public String getLine(int lineNumber) {
  String js = "";
  try {
    js = getCode();
  } catch (IOException e) {
    return null;
  }

  // Utilize existing method to get the line offset
  int pos;
  try {
    pos = getLineOffset(lineNumber);
  } catch (IllegalArgumentException e) {
    return null; // If the line number is out of range, return null
  }

  int endPos = js.indexOf('\n', pos);
  if (endPos == -1) {
    // If there is no newline, return the substring from pos to the end of the string
    return js.substring(pos);
  } else {
    // Return the substring from pos to the next newline
    return js.substring(pos, endPos);
  }
}