private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  
  Preconditions.checkArgument(node.isGetProp());
  
  while (node != null && !node.isAssign()) {
      node = node.getNext();
  }
  
  if (node == null || !node.isAssign() || !node.getFirstChild().equals(node)) {
    // If the current node is not an assignment, check its children.
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (!isSafeReplacement(child, replacement)) {
        return false;
      }
    }
  } else {
    Node target = node.getFirstChild();
    String name = node.getString();
    
    // Check if the assignment is to a complex expression involving the original node.
    if (isComplexExpression(target)) {
      return !hasReferenceToOriginalNode(target, replacement);
    }
    
    // If the assignment is to a simple name and it's assigned to the replacement node,
    // then we can't replace the original node with the replacement node.
    if (target.isName() && target.getString().equals(name) 
        && isNameAssignedTo(name, replacement)) {
      return false;
    }
  }

  return true;
}

private boolean isComplexExpression(Node node) {
  // Check for complex expressions involving nested property access
  while (node != null) {
    if (node.isGetProp()) {
      break;
    } else if (node.isName() && hasNestedPropertyAccess(node.getString())) {
      return true;
    }
    node = node.getNext();
  }
  
  return false;
}

private boolean hasReferenceToOriginalNode(Node node, Node replacement) {
  // Check if the complex expression has a reference to the original node
  for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
    if (!child.isName() && !hasReferenceToOriginalNode(child, replacement)) {
      return false;
    }
  }
  
  // If we've reached this point, it means that the complex expression has a reference to the original node
  return true;
}

private boolean hasNestedPropertyAccess(String name) {
  // Check if the string represents a nested property access (e.g., "parentNode.parentNode")
  int index = name.lastIndexOf('.');
  
  return index != -1 && !name.substring(index + 1).equals("parent");
}