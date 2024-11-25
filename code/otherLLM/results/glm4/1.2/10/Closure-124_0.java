private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
      return true;
    }
    Preconditions.checkArgument(node.isGetProp());

    Node currentNode = node.getFirstChild();
    while (currentNode != null) {
        // Check for name assignment to the replacement
        if (currentNode.isName() && isNameAssignedTo(currentNode.getString(), replacement)) {
            return false;
        }

        // Move to the next child
        currentNode = currentNode.getNext();
    }

    return true;
}

private boolean isNameAssignedTo(String name, Node node) {
    for (Node c = node.getFirstChild(); c != null; c = c.getNext()) {
        if (isNameAssignedTo(name, c)) {
            return true;
        }
    }

    if (node.isName()) {
        Node parent = node.getParent();
        if (parent.isAssign() && parent.getFirstChild() == node) {
            if (name.equals(node.getString())) {
                return true;
            }
        }
    }

    return false;
}