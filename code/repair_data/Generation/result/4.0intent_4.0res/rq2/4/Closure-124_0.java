private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    Preconditions.checkArgument(node.isGetProp());

    Node currentNode = node.getFirstChild();
    if (currentNode.isName() && isNameAssignedTo(currentNode.getString(), replacement)) {
        return false;
    }

    // Check to ensure replacement is not any ancestor of the node
    Node parent = node.getParent();
    while (parent != null) {
        if (parent.equals(replacement)) {
            return false;
        }
        parent = parent.getParent();
    }

    return true;
}