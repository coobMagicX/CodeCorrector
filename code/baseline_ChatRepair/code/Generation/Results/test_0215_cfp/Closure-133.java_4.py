private String getRemainingJSDocLine() {
    String result = stream.getRemainingJSDocLine();
    if (result != null) {
        // Trim the result and remove asterisk that may be used for JSDoc formatting
        result = result.trim();
        result = result.replaceAll("\\s*\\*\\s*$", ""); 
    }
    return result;
