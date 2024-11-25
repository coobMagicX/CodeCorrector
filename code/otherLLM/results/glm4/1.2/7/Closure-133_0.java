private String getRemainingJSDocLine() {
    StringBuilder sb = new StringBuilder();
    boolean inJSDocComment = false;

    // Assuming 'stream' is an object with a method 'getLine()' that returns the next line of code or comment.
    while (stream.hasMoreLines()) {
        String line = stream.nextLine(); // This should get the next line from the stream.
        
        if (!inJSDocComment && line.contains("/**") && !line.endsWith("*/")) {
            inJSDocComment = true;
            sb.append(line).append("\n");
        } else if (inJSDocComment) {
            if (line.contains("*/")) {
                inJSDocComment = false; // End of JSDoc comment
            } else {
                sb.append(line).append("\n");
            }
        }

        // If the stream is not in a JSDoc comment, we can return the remaining code.
        if (!inJSDocComment) {
            break;
        }
    }
    
    // Remove trailing newline character if present
    if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
        sb.setLength(sb.length() - 1);
    }

    return sb.toString();
}