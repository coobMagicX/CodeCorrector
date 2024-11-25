private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();
  
  if (isAssignmentTarget(n)) {
    return n;
  }

  if (!right.isNumber()) {
    // Sometimes people like to use complex expressions to index into arrays, or strings to index into array methods.
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) Math.floor(index); // Use floor instead of casting directly to ensure integer part is taken

  if (intIndex != index && !Double.isInteger(index)) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  if (intIndex < 0) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;
  
  for (int i = 0; current != null && i <= intIndex; i++) { // Include the index check in the loop to handle the edge case where 'i' equals 'intIndex'
    if (current == null) {
      error(INDEX_OUT_OF_BOUNDS_ERROR, right);
      return n;
    }
    
    elem = current;

    current = current.getNext();
  }

  if (elem == null || i > intIndex) { // Add a check after the loop to ensure we don't go past the intended index
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  if (elem.isEmpty()) {
    elem = NodeUtil.newUndefinedNode(elem); // Ensure that we create a new node with 'undefined' value for empty nodes
  } else {
    left.removeChild(elem); // Remove the element from its original position
  }

  // Replace the entire GETELEM with the value
  n.getParent().replaceChild(n, elem);
  reportCodeChange();
  return elem;
}