private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();

  // Don't fold if GETPROP/GETELEM is used as an assignment target.
  if (isAssignmentTarget(n)) {
    return n;
  }

  // Only proceed if the index is a number.
  if (!right.isNumber()) {
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) index;
  // Check for non-integer indices.
  if (intIndex != index) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  // Non-positive index checking.
  if (intIndex < 0) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  int currentIndex = 0;

  while (current != null && currentIndex < intIndex) {
    currentIndex++;
    current = current.getNext();
  }

  // If element at the specified index doesn't exist or index is out-of-bounds.
  if (current == null) {
    // Reporting an index out of bounds error.
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  // If found element is empty, replace with undefined.
  if (current.isEmpty()) {
    current = NodeUtil.newUndefinedNode(n);
  } else {
    left.removeChild(current);
  }

  // Replace the entire GETELEM with the resolved value.
  parent.replaceChild(n, current);
  reportCodeChange();
  return current;
}
