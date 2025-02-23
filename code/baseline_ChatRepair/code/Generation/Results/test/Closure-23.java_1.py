private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  if (isAssignmentTarget(n)) {
    return n;
  }

  if (!right.isNumber()) {
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
  for (int i = 0; current != null; i++) {
    if (i == intIndex) {
      elem = current;
      if (elem.isEmpty()) {
        // Handle sparse array elements
        elem = NodeUtil.newUndefinedNode(null);
      }
      break;
    }
    current = current.getNext();
  }

  if (elem == null) {
    // Handle cases when index is out of the actual bounds of the array
    elem = NodeUtil.newUndefinedNode(null);
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
  } else if (!elem.isEmpty()) {
    left.removeChild(elem);
  }

  // Replace the entire GETELEM with the value
  n.getParent().replaceChild(n, elem);
  reportCodeChange();
  return elem;
}
