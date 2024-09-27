private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    Preconditions.checkArgument(node.isGetProp());

    Node originalNode = node;
    node = node.getFirstChild();
    if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
        return false;
    }

    // Check if replacement is a parent or ancestor of the node.
    Node parentNode = originalNode.getParent();
    while (parentNode != null) {
        if (parentNode.equals(replacement)) {
            return false;
        }
        parentNode = parentNode.getParent();
    }

    // Additional check to handle specific issues related to assignments in complex expressions
    if (originalNode.isAssign() && originalNode.getFirstChild().equals(replacement)) {
        return false;
    }

    return true;
}