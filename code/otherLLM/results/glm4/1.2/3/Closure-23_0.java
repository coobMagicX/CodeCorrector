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
  for (int i = 0; current != null && i <= intIndex; i++) { // Change condition to <= to handle equal bounds
    if (i < intIndex) {
      current = current.getNext(); // Move to the next element before checking for bounds
    } else if (current == null) {
      error(INDEX_OUT_OF_BOUNDS_ERROR, right);
      return n;
    }

    elem = current; // Assign current to elem at the end of the iteration
  }

  if (elem == null || !left.isScope()) { // Check if left is not a scope before proceeding
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  if (elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(elem); // Use NodeUtil to create an undefined node
  } else {
    left.removeChild(elem); // Remove the element from its parent
  }

  // Replace the entire GETELEM with the value or undefined node
  n.getParent().replaceChild(n, elem);
  reportCodeChange();
  return elem;
}