private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();

  if (isAssignmentTarget(n)) {
    return n;
  }

  if (!right.isNumber()) {
    // Sometimes people like to use complex expressions to index into arrays,
    // or strings to index into array methods.
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) index;
  if (intIndex != index || intIndex < 0) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;

  for (int i = 0; current != null && i <= intIndex; i++) { // Include equality to handle the case when i == intIndex
      if (i > 0) { // Initialize elem at the start of the loop after the first iteration
          elem = current;
      }
      
      current = current.getNext();
  }

  if (elem == null || current != null && current != left.getLastChild()) { // Check for valid index and last child boundary
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  if (elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(elem);
  } else {
    left.removeChild(elem);
  }

  // Replace the entire GETELEM with the value
  n.getParent().replaceChild(n, elem);
  reportCodeChange();
  return elem;
}