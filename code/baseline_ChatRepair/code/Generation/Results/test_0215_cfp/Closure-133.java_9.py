private String getRemainingJSDocLine() {
    StringBuilder result = new StringBuilder();
    String line;
    while ((line = stream.getRemainingJSDocLine()) != null) {
        // Trim