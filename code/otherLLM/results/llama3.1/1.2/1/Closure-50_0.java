/**
 * Try to fold an array join: ['a', 'b', 'c'].join('') -> 'abc';
 */
private Node tryFoldArrayJoin(Node n) {
    // Check if the node is an array
    if (n.getType() != Type.ARRAY) {
        return n;  // Not a valid array, skip optimization
    }

    // Get the first and last elements of the array
    Node first = n.getFirst();
    Node last = n.getLast();

    // If the first element is not a string or the join method has no arguments,
    // we cannot fold this expression.
    if (first.getType() != Type.STRING || !hasArguments(n)) {
        return n;  // Not enough information to optimize
    }

    // Get the string value of the first element and the join string
    String str = NodeUtil.getStringValue(first);
    String joinStr = "";

    // If the array has a second argument, use it as the join string.
    if (hasArguments(n)) {
        Node arg = n.getSecond();
        joinStr = NodeUtil.getStringValue(arg);
    }

    // Construct the joined string
    StringBuilder sb = new StringBuilder();
    for (Node elem : n.getChildren()) {
        sb.append(NodeUtil.getStringValue(elem));
    }
    String result = sb.toString();

    if (!joinStr.isEmpty() && !result.contains(joinStr)) {
        return n;  // The join string is not present in the joined string, skip optimization.
    }

    // Create a new number node with the length of the joined string
    Node newNode = Node.newNumber(result.length());

    // Replace the original array call with the new number node
    n.getParent().replaceChild(n, newNode);

    return newNode;
}

// Helper method to check if a node has arguments
private boolean hasArguments(Node n) {
    return n.getSecond() != null && n.getThird() == null;
}