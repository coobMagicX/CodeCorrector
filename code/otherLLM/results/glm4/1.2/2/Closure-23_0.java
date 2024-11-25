private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();
  // If GETPROP/GETELEM is used as assignment target the array literal is
  // acting as a temporary we can't fold it here:
  //    "[][0] += 1"
  if (isAssignmentTarget(n)) {
    return n;
  }

  if (!right.isNumber()) {
    // Sometimes people like to use complex expressions to index into
    // arrays, or strings to index into array methods.
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) Math.round(index); // Use Math.round to round the index value to nearest integer
  if (Math.abs(intIndex - index) > 0.000001) { // Check for floating-point rounding errors
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  if (intIndex < 0) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  Node elem = null;
  for (int i = 0; current != null && i <= intIndex; i++) { // Include the index in the loop condition to handle integer rounding
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
    elem = NodeUtil.newUndefinedNode(elem); // Correctly create an undefined node
  } else {
    left.removeChild(elem);
  }

  // Replace the entire GETELEM with the value or a placeholder for the array element
  Node replacementNode = new Node(Token.NUMBER, Double.toString(intIndex)); // Create a new number node with the index as its value
  n.getParent().replaceChild(n, replacementNode); // Replace the GETELEM with the index node
  reportCodeChange();
  return replacementNode;
}