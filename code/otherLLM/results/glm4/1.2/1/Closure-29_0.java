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

      Node objectNode = ref.getAssignedValue();
      if (objectNode != null && !objectNode.isObjectLit()) {
        // If the property is not defined on the object literal, we should check for undefined properties
        boolean hasUndefinedProperty = objectNode.getProperties().stream()
            .anyMatch(prop -> prop.getRight() == null);
        if (hasUndefinedProperty) {
          return false; // Property is undefined or has no value assigned
        }
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
      if (childVal == null) continue;

      // Check if childVal is the parent of any of the passed in references
      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal) {
            return false;
          }
          refNode = refNode.getParent();
        }
      }
    }

    ret = true; // We found an acceptable object literal assignment
  }
  return ret;
}