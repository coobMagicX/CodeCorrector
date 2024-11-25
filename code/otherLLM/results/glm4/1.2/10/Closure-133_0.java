private String getRemainingJSDocLine() {
    try {
        Node typeNode = parseResultType(stream.peekToken());
        if (typeNode != null) {
            stream.skipEOLs();
            return stream.getRemainingJSDocLine();
        } else {
            // Assuming there's a method to create an error message
            return createErrorMessage("Syntax error in type annotation");
        }
    } catch (Exception e) {
        // Assuming there's a method to create an error message from the exception
        return createErrorMessage(e.getMessage());
    }
}

private String createErrorMessage(String message) {
    // This is a placeholder for creating an appropriate error message.
    // The actual implementation would depend on how you want to format or handle errors in your system.
    return "Error: " + message;
}