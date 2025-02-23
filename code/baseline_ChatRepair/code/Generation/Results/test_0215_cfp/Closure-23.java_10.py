private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();
  // Return early if n is used as an assignment target.
  if (isAssignmentTarget(n)) {
    return n;
  }

  // Only optimize for numeric indices.
  if (!right.isNumber()) {
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) index;
  if (intIndex != index) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  // Negative indices are not supported in array accesses.
  if (intIndex < 0) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;
  int currentIndex = 0;
  
  // Traverse children to find the element at the specified index.
  while (current != null && currentIndex <= intIndex) {
    if (currentIndex == intIndex) {
      elem = current;
      break;
    }
    current = current.getNext();
    currentIndex++;
  }

  // If elem is not found, the index is out of bounds.
  if (elem == null) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  // Create 'undefined' node if accessed element is an empty node.
  if (elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(null);
  } else {
    left.removeChild(elem);
  }

  // Replace the entire GETELEM node with the extracted element node.
  parent.replaceChild(n, elem);
  reportCodeChange();
  return elem;
}
