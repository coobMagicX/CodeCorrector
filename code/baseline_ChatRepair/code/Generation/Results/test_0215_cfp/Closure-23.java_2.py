private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  // Preliminary checks.
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

  // Handle arrays, like [,10], where the first element is intentionally undefined.
  Node current = left.getFirstChild();
  Node elem = null;
  int count = 0;
  while (current != null) {
    if (count == intIndex) {
      // Found the correct indexed element.
      elem = current;
      break;
    }
    count++;
    current = current.getNext();
  }

  // Handle cases when the index exceeds the array length or the element is not defined.
  if (elem == null || elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(null); // Generate an 'undefined' node.
  } else {
    // The element must be safely detachable.
    if (elem.getParent() == left) {
      left.removeChild(elem);
    }
  }

  // Replace the GETELEM node with the element.
  parent = n.getParent();
  if (parent != null) {
    parent.replaceChild(n, elem);
    reportCodeChange();
  }
  return elem;
}
