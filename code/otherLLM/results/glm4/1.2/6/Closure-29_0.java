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

      // Short-circuit if property is not defined on the object literal.
      Node propDef = ref.getPropDef();
      if (propDef != null && !propDef.getParent().isObjectLit()) {
        continue; // Property reference is undefined or not on object literal
      }
    }

    if (!isVarOrAssignExprLhs(name)) {
       return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      continue; // A var with no assignment.
    }

    if (!val.isObjectLit()) {
      return false; // Not an object literal assignment.
    }

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false; // ES5 get/set not supported.
      }

      Node childVal = child.getFirstChild();
      if (isSelfReferential(refs, name, childVal)) {
        return false; // Self-referential assignment detected.
      }
    }

    ret = true;
  }
  return ret;
}

private boolean isSelfReferential(List<Reference> refs, Node baseNode, Node candidate) {
  for (Reference t : refs) {
    if (t.getNode() == baseNode) {
      Node current = t.getParent();
      while (!NodeUtil.isStatementBlock(current)) {
        if (current == candidate) {
          return true; // Self-referential assignment detected.
        }
        current = current.getParent();
      }
    }
  }
  return false;
}