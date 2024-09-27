private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    Preconditions.checkArgument(node.isGetProp());

    Node parent = replacement.getParentNode();
    Node grandparent = parent != null ? parent.getParentNode() : null;

    if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
        return false;
    }

    if (parent != null && node.isEquivalentTo(parent)) {
        return false;
    }

    if (grandparent != null && node.isEquivalentTo(grandparent)) {
        return false;
    }

    return true;
}