private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Ignore indirect references, like x.y (except x.y(), since
    // the function referenced by y might reference 'this').
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (gramps.isCall()
          && gramps.getFirstChild() == parent) {
        return false;
      }
      continue; // Skip getting properties as they are not object literals
    }

    if (!isVarOrAssignExprLhs(name)) {
      return false; // Only VAR declarations or simple assignment statements are considered
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      continue; // A var with no assignment is skipped
    }

    if (!val.isObjectLit()) {
      return false; // Not an object literal assignment
    }

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false; // ES5 getters/setters not supported
      }
      
      Node childVal = child.getFirstChild();
      boolean isSelfReferential = false;
      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal || NodeUtil.isSameOrParentOf(refNode, childVal)) {
            // Check for self-referential assignments
            isSelfReferential = true;
            break;
          }
          refNode = refNode.getParent();
        }
        if (isSelfReferential) {
          break;
        }
      }

      if (isSelfReferential) {
        return false; // Self-referential assignment detected, cannot inline
      }
    }

    ret = true; // Found an acceptable object literal assignment
  }
  return ret;
}

// Helper method to check if a node is the same or a parent of another node
private boolean NodeUtil.isSameOrParentOf(Node node1, Node node2) {
  while (node1 != null) {
    if (node1 == node2) {
      return true;
    }
    node1 = node1.getParent();
  }
  return false;
}