private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

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
    if (val == null || !val.isObjectLit()) {
      return false;
    }

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false;
      }

      Node childVal = child.getFirstChild();
      for (Reference t : refs) {
        if (isReferringReference(t, childVal)) {
          return false;
        }
      }
    }

    ret = true;
  }
  return ret;
}

private boolean isReferringReference(Reference ref, Node target) {
  Node refNode = ref.getParent();
  while (!NodeUtil.isStatementBlock(refNode)) {
    if (refNode == target) {
      return true;
    }
    refNode = refNode.getParent();
  }
  return false;
}
