private String getRemainingJSDocLine() {
    String result = stream.getRemainingJSDocLine();
    
    // Check if the line starts with a comment character and is not just whitespace.
    if (result != null && !result.trim().isEmpty() && (result.startsWith("//") || 
            (result.contains("@") && (result.indexOf('@') == 0 || result.indexOf('@') == 1)))) {
        int index = result.indexOf('\n'); // Find the end of the current JSDoc line.
        if (index >= 0) {
            return result.substring(0, index); // Return only up to the newline character.
        }
    }

    // If the line is null or empty, or not a valid JSDoc line, return an empty string.
    return "";
}