private String getRemainingJSDocLine() {
    StringBuilder result = new StringBuilder();
    try {
        // Assuming that stream.getRemainingJSDocLine() returns a single line of the JSDoc comment.
        String line = stream.getRemainingJSDocLine();
        
        // Check for common JSDoc comment indicators
        if (line.startsWith("/**") || line.startsWith("/ *")) {
            // Add this line to the result StringBuilder, assuming it's part of a valid JSDoc comment
            result.append(line);
        } else if (!line.isEmpty()) {
            // If the line does not start with a valid JSDoc indicator and is not empty,
            // we might have an error in the JSDoc syntax.
            throw new IllegalArgumentException("Invalid or incomplete JSDoc comment.");
        }
    } catch (Exception e) {
        // Log the exception, rethrow it, or handle it as appropriate for your environment
        System.err.println("Error parsing JSDoc line: " + e.getMessage());
        throw new RuntimeException("Failed to parse JSDoc line.", e);
    }

    return result.toString();
}