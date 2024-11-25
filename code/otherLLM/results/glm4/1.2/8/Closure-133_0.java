private String getRemainingJSDocLine() {
    if (stream == null) {
        throw new IllegalStateException("Stream is not initialized");
    }

    StringBuilder result = new StringBuilder();
    boolean inComment = false;

    while (stream.hasNext()) {
        String token = stream.next();
        
        // Check for the start of a JSDoc comment
        if (!inComment && "/**".equals(token)) {
            inComment = true;
        }
        // If we are inside a JSDoc comment, append tokens until we reach the end of the line or comment block
        else if (inComment) {
            if ("*/".equals(token)) { // End of the JSDoc comment
                break;
            }
            
            // Ignore whitespace and continue processing
            if (!token.trim().isEmpty()) {
                result.append(token);
            }
        } else {
            // If we encounter a non-comment token, reset the state
            inComment = false;
        }
    }

    return result.toString();
}