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

  // Check for integer conversion loss
  if (index != intIndex && !Double.isNaN(index)) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  // Check for negative index
  if (intIndex < 0) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;

  // Corrected loop to ensure that `elem` is assigned before `current` is advanced
  for (int i = 0; current != null && i <= intIndex; i++) {
    if (i == intIndex) {
      elem = current;
    }
    current = current.getNext();
  }

  // If no element found, report an error
  if (elem == null) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  // Replace the GETELEM node with its value if it's not empty
  if (!elem.isEmpty()) {
    left.removeChild(elem);
    reportCodeChange();
  } else {
    elem = NodeUtil.newUndefinedNode(elem);
    reportCodeChange();
  }

  parent.replaceChild(n, elem);
  return elem;
}