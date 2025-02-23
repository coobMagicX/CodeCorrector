private String getRemainingJSDocLine() {
  if (stream.hasNextLine()) {
    return stream.getRemainingJSDocLine();
  } else {
    return ""; // Return an empty string if there is no