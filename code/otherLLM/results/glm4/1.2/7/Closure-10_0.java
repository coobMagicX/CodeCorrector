static boolean mayBeString(Node n, boolean recurse) {
  if (recurse) {
    // Ensure that all children of the node also need to be checked recursively.
    for (Node child : n.children()) {
      if (!mayBeStringHelper(child, true)) { // Pass true to check recursively for child nodes
        return false;
      }
    }
    return allResultsMatch(n, MAY_BE_STRING_PREDICATE);
  } else {
    return mayBeStringHelper(n);
  }
}

static boolean mayBeStringHelper(Node n, boolean recurse) {
    if (n == null || isNumericResult(n) || isBooleanResult(n)
        || isUndefined(n) || n.isNull()) {
      return false;
    }

    for (Node child : n.children()) {
      // If recursive check is enabled and the child is not a leaf node, 
      // we need to recursively verify the children.
      if (recurse && !isLeafNode(child)) {
        if (!mayBeStringHelper(child, true)) { // Pass true for recursion
          return false;
        }
      } else {
        if (!isNumericResult(child) && !isBooleanResult(child)
            && !isUndefined(child) && !child.isNull()) {
          // If the child is not a leaf node and we are not in recursive mode,
          // then it can't be purely a string, as we don't evaluate its children.
          if (!isLeafNode(child)) {
            return false;
          }
        }
      }
    }

    return true; // The node passes all checks.
}

// Helper method to check if a node is a leaf node
static boolean isLeafNode(Node n) {
    return !n.isParent();
}