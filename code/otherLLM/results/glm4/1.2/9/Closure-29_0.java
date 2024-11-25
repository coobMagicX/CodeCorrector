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
      continue;
    }

    // Only rewrite VAR declarations or simple assignment statements
    if (!isVarOrAssignExprLhs(name)) {
       return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null || !val.isObjectLit()) {
      return false;
    }

    // Make sure that the value is not self-refential.
    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() ||
          child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }

      Node childVal = child.getFirstChild();
      if (isSelfReferential(name, childVal, refs)) {
        return false; // This handles the self-referential assignments
      }
    }

    ret = true;
  }
  return ret;
}

// Helper method to determine if there is a self-referential assignment
private boolean isSelfReferential(Node name, Node childVal, List<Reference> refs) {
  for (Reference t : refs) {
    Node refNode = t.getParent();
    while (!NodeUtil.isStatementBlock(refNode)) {
      if (refNode == childVal || refNode == name) {
        // We have a self-referential assignment
        return true;
      }
      refNode = refNode.getParent();
    }
  }
  return false;
}