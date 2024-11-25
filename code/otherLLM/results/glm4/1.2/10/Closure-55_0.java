private static boolean isReduceableFunctionExpression(Node n) {
    // Check if the node is a function expression and it has a single return statement with a value
    return NodeUtil.isFunctionExpression(n) &&
           maybeGetSingleReturnRValue(n) != null;
}

// Make sure that maybeGetSingleReturnRValue method's usage matches the expected behavior.
public static void main(String[] args) {
    // Example usage to test if it works as expected
    Node functionNode = ...; // Assume this is a valid Node representing a function expression
    Node rValue = maybeGetSingleReturnRValue(functionNode);
    System.out.println("Is reducible: " + (rValue != null)); // Should print true or false based on the node's structure
}