private String getRemainingJSDocLine() {
  if (stream == null) { // validate input parameter
    throw new NullPointerException("Stream object cannot be null");
  }
  
  try {
    String result = stream.getRemainingJSDocLine();
    return result;
  } catch (Exception e) { // handle potential exceptions
    log.error("Error parsing JSDoc line", e);
    return null; // or some default value
  }
}