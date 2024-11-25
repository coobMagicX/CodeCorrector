private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    Preconditions.checkArgument(node.isGetProp());

    while (node != null && !node.isAssign()) { // Move to the assignment parent
        node = node.getParent();
    }

    if (node == null || !node.isAssign()) {
        throw new IllegalArgumentException("Node is not an assign operation.");
    }

    Node firstChild = node.getFirstChild();

    while (firstChild != null) {
        if (firstChild.isName() && isNameAssignedTo(firstChild.getString(), replacement)) {
            return false;
        }
        firstChild = firstChild.getNext();
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