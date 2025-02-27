private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();

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
  int currentIndex = 0;
  while (current != null) {
    if (currentIndex == intIndex) {
      elem = current;
      break;
    }
    currentIndex++;
    current = current.getNext();
  }

  if (elem == null) {
    // Handle case where indices are greater than the items in the array literal including sparse items
    if (intIndex >= currentIndex) {
      error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    }
    elem = NodeUtil.newUndefinedNode(n);
  } else if (elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(elem);
  } else {
    left.removeChild(elem);
  }

  n.getParent().replaceChild(n, elem);
  reportCodeChange();
  return elem;
}
