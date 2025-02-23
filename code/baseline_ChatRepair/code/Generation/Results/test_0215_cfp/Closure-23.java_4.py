private Node tryFoldArrayAccess(Node n, Node left, Node right) {
  Node parent = n.getParent();

  // If GETPROP/GETELEM is used as assignment target, we do not fold.
  if (isAssignmentTarget(n)) {
    return n;
  }

  // Only fold if index is a number.
  if (!right.isNumber()) {
    return n;
  }

  double index = right.getDouble();
  int intIndex = (int) index;
  // Check for non-integer index.
  if (intIndex != index) {
    error(INVALID_GETELEM_INDEX_ERROR, right);
    return n;
  }

  // Check for negative index.
  if (intIndex < 0) {
    error(INDEX_OUT_OF_BOUNDS_ERROR, right);
    return n;
  }

  Node current = left.getFirstChild();
  int currentIndex = 0;

  // The following loop should skip index positions with side-effects.
  while (current != null) {
    // Only allow folding when safely ignoring side effects.
    if (NodeUtil.mayHaveSideEffects(current)) {
      if (currentIndex == intIndex) {
        return n; // Cannot replace due to potential side effects.
      }
    } else if (currentIndex == intIndex) {
      break; // Found the indexable node without side effects.
    }
    currentIndex++;
    current = current.getNext();
  }

  // If an element at the index was not found or has side effects.
  if (current == null || NodeUtil.mayHaveSideEffects(current)) {
    return n;
  }

  if (current.isEmpty()) {
    current = NodeUtil.newUndefinedNode(n);
  } else {
    left.removeChild(current);
  }

  // Replace the whole GETELEM node.
  parent.replaceChild(n, current);
  reportCodeChange();
  return current;
}
