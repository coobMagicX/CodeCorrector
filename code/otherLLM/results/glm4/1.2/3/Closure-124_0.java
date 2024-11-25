private boolean isSafeReplacement(Node node, Node replacement) {
  // No checks are needed for simple names.
  if (node.isName()) {
    return true;
  }
  Preconditions.checkArgument(node.isGetProp());

  Node parentNode = node.getParent();
  Set<Node> visitedNodes = new HashSet<>();
  Stack<Node> stack = new Stack<>();

  stack.push(parentNode);

  while (!stack.isEmpty()) {
    Node current = stack.pop();

    // Check for cycles in the parent chain
    if (visitedNodes.contains(current)) {
      return false; // A cycle is detected, indicating a potential infinite loop or recursion.
    }

    visitedNodes.add(current);

    if (current.isName() && isNameAssignedTo(current.getString(), replacement)) {
      return false;
    }

    if (current != node) { // Avoid pushing the original node to avoid unnecessary processing
      stack.push(current);
    }

    // Move to the first child of the current node to continue checking
    node = node.getFirstChild();
  }

  return true; // No cycles found, and no unsafe replacement detected.
}