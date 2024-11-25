private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
      return true;
    }
    Preconditions.checkArgument(node.isGetProp());

    // Iterate through the children of the current node to check for repeated assignments.
    Node child = node.getFirstChild();
    while (child != null) {
        if (child.isName() && isNameAssignedTo(child.getString(), replacement)) {
            return false; // Name already assigned to another variable in replacement
        }
        child = child.getNext();
    }

    return true;
}