private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
  
    Preconditions.checkArgument(node.isGetProp(), "Node must be a property getter.");
  
    node = node.getFirstChild();
    if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
        return false;
    }
  
    return true;
}