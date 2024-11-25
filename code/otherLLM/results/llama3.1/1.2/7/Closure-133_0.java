private String getRemainingJSDocLine() {
  if (!stream.isValid()) { // assume 'isValid()' method exists to validate the stream contents
    throw new IllegalStateException("Invalid stream contents");
  }
  
  String line = "";
  while ((line = stream.getLine()) != null) {
    int docCommentStartIndex = findDocCommentStart(line);
    if (docCommentStartIndex != -1) { // assume 'findDocCommentStart()' method exists to extract JSDoc lines
      return line.substring(docCommentStartIndex); 
    }
  }

  String result = stream.getRemainingJSDocLine();
  return result;
}