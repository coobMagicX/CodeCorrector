private String getRemainingJSDocLine() {
    StringBuilder resultBuilder = new StringBuilder();
    Node currentNode = stream.getCurrentNode();

    while (currentNode != null && currentNode.getType() == Token.JSDOC_LINE) {
        // Assuming newNode is a method that creates a new node instance with the text from the JsDoc token
        Node newNode = newNode(currentNode);
        if (newNode != null) {
            resultBuilder.append(newNode.getText());
        } else {
            break; // Break the loop if newNode creation fails, indicating an error in JsDoc syntax
        }
        currentNode = stream.next();
    }

    // Validate and handle end of JSDoc block
    if (!currentNode.getType().equals(Token.JSDOC_BLOCK_END)) {
        throw new IllegalArgumentException("Invalid JSDoc format");
    }

    return resultBuilder.toString();
}

// Assuming newNode is defined as follows:
private Node newNode(Node node) {
    // Implementation to create a new node with the text from the given JsDoc token
    // This method should be defined elsewhere in your class or imported if it's part of another library.
}