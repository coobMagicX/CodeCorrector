private boolean isSafeReplacement(Node node, Node replacement) {
  if (node.isName()) {
    // If it is a simple name, check if it directly gets assigned to the replacement.
    return !isNameAssignedTo(node.getString(), replacement);
  }
  Preconditions.checkArgument(node.isGetProp());

  Node child = node.getFirstChild();
  // If node is a property access, ensure the object being accessed is not altered by the replacement.
  // Additionally, check that any property in the chain is not causing aliasing issues.
  while (!child.isName()) {
    if (child.isGetProp()) {
      child = child.getFirstChild();
    } else {
      // If we encounter a node type that is neither a name nor a property accessor,
      // assuming further checks are unnecessary
      return true;
    }
  }

  // At the point where child is a Name type.
  if (isNameAssignedTo(child.getString