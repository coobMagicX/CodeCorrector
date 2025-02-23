private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();
  // Identify if the array access is being used as a target for assignment.
  if (isAssignmentTarget(n)) {
    return n;
  }

  // Check if the index is not a simple number.
  if (!right.isNumber()) {
    return n;
  }

  // Pull out the index as a double and convert it to an integer.
  double index = right.getDouble();
  int intIndex = (int) index;

  // Ensure that the index is an integer and that it doesn't exceed array bounds.
  if (intIndex != index) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  // Initialize indexing to fetch the corresponding element.
  Node current = left.getFirstChild();
  Node elem = null;
  int count = 0; // To count elements, not merely iterate based on index.

  // Loop through children to get to the correct index.
  while (current != null) {
    if (count == intIndex && !current.isEmpty()) {
      elem = current;
      break;
    }
    current = current.getNext();
    count++;
  }

  // If no element has been assigned to 'elem', report an out-of-bounds error
  if (elem == null) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  // Handle empty nodes correctly by returning an 'undefined' node.
  if (elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(null);
  } else {
    // Ensure the element is removed from its parent if it's being re-used.
    left.removeChild(elem);
  }

  // Replace the whole GETELEM node with the retrieved element.
  parent.replaceChild(n, elem);
  reportCodeChange();
  return elem;
}
