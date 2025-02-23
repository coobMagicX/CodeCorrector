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
  if (intIndex != index || intIndex < 0) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;
  for (int i = 0; current != null; i++) {
    if (i == intIndex) {
      elem = current;
      break;
    }
    current = current.getNext();
  }

  if (elem == null) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  if (elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(null);
  } else {
    left.removeChild(elem);
  }

  parent.replaceChild(n, elem);
  reportCodeChange();
  return elem;
}
