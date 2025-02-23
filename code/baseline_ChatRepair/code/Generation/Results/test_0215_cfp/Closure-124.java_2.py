private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks needed for simple names except checking if being assigned.
    if (node.isName()) {
        String nodeName = node.getString();
        return !isReplacementUnsafe(nodeName, replacement);
    }

    Preconditions.checkArgument(node.isGetProp(), "Expected property node");

    // Traverse the object chain to validate no part is assigned to from the replacement.
    while (node.isGetProp()) {
        node = node.getFirstChild();
        if (node.isName() && isReplacementUnsafe(node.getString(), replacement)) {
            return false;
        }
    }

    return true;
}

private boolean isReplacementUnsafe(String nodeName, Node replacement) {
    if (replacement.isName()) {
        String replacementName = replacement.getString();
        // If the replacement is a simple name, just compare the names
        return nodeName.equals(replacementName);
    } else if (replacement.isGetProp()) {
        // If the replacement is a property access, check if the base object is unsafe
        Node base = replacement.getFirstChild();
        while (base.isGetProp()) {
            base = base.getFirstChild();
        }
        if (base