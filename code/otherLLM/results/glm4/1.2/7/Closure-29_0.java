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
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }
      continue;
    }

    if (!isVarOrAssignExprLhs(name)) {
      return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      // A var with no assignment.
      continue;
    }

    if (!val.isObjectLit()) {
      return false;
    }

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        // ES5 get/set not supported.
        return false;
      }
      Node childVal = child.getFirstChild();
      if (isSelfReferentialAssignment(childVal, refs)) {
        // There's a self-referential assignment
        return false;
      }
    }

    // We have found an acceptable object literal assignment. As
    // long as there are no other assignments that mess things up,
    // we can inline.
    ret = true;
  }
  return ret;
}

private boolean isSelfReferentialAssignment(Node childVal, List<Reference> refs) {
  for (Reference t : refs) {
    Node refNode = t.getParent();
    while (!NodeUtil.isStatementBlock(refNode)) {
      if (refNode == childVal) {
        // There's a self-referential assignment
        return true;
      }
      refNode = refNode.getParent();
    }
  }
  return false;
}