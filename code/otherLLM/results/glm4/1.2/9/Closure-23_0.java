private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();
  // If GETPROP/GETELEM is used as assignment target the array literal is
  // acting as a temporary we can't fold it here:
  //    "[][0] += 1"
  if (isAssignmentTarget(n)) {
    return n;
  }

  if (!right.isNumber()) {
    // Sometimes people like to use complex expressions to index into
    // arrays, or strings to index into array methods.
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) index;
  if (intIndex != index) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  if (intIndex < 0) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;
  for (int i = 0; current != null && i < intIndex; i++) {
    current = current.getNext(); // Move to the next element before checking for elem
  }

  if (current == null) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  // Now that we have reached the desired index, check if it's an empty node
  if (!current.isEmpty()) {
    if (current.getFirstChild() != null && current.getLastChild() == null) {
      // The element is not a leaf node and thus cannot be replaced with its value.
      return n;
    }
    // Replace the current node with its value
    Node valueNode = current.getFirstChild();
    parent.replaceChild(n, valueNode);
    reportCodeChange();
    return valueNode;
  } else {
    error(INVALID_GETELEM_INDEX_ERROR, right); // Error if the element is empty
    return n;
  }
}