private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();
      if (!validProperties.contains(propName)) {
        if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
          validProperties.add(propName);
        } else {
          return false;
        }
      }
    } else if (isVarOrAssignExprLhs(name)) {
      Node val = ref.getAssignedValue();
      if (val == null) {
        continue; // A var with no assignment is fine.
      }

      if (!val.isObjectLit()) {
        return false;
      }

      for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
        if (child.isGetterDef() || child.isSetterDef()) {
          return false;
        }
        validProperties.add(child.getString());

        Node childVal = child.getFirstChild();
        if (isSelfReferentialAssignment(refs, childVal)) {
          return false;
        }
      }

      ret = true; // Found an object literal assignment.
    } else {
      return false; // Invalid left-hand side of variable or assignment expression.
    }
  }
  return ret;
}

private boolean isSelfReferentialAssignment(List<Reference> refs, Node childVal) {
  for (Reference ref : refs) {
    Node parent = ref.getParent();
    while (!NodeUtil.isStatementBlock(parent)) {
      if (parent == childVal) {
        return true; // Found a self-referential assignment.
      }
      parent = parent.getParent();
    }
  }
  return false; // No self-referential assignment found.
}