private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();
  
  if (isAssignmentTarget(n)) {
    return n;
  }

  if (!right.isNumber()) {
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) Math.round(index);
  if (Math.abs(intIndex - index) > 0.000001) { // Using a tolerance for floating-point inaccuracies
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  if (intIndex < 0 || intIndex >= left.getChildCount()) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;
  for (int i = 0; current != null && i <= intIndex; i++) { // Fix the loop to allow reaching the correct index
    if (i == intIndex) {
      elem = current;
      break;
    }
    current = current.getNext();
  }

  if (elem == null || elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(elem);
  } else {
    left.removeChild(elem);
  }

  n.getParent().replaceChild(n, elem);
  reportCodeChange();
  return elem;
}