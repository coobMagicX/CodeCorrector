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
        // Check if the property is not deleted or defined
        if (!NodeUtil.isVarOrSimpleAssignLhs(parent, gramps)) {
          return false; // The property should not be inlined
        }
        validProperties.add(propName);
      }
      continue;
    }

    if (!isVarOrAssignExprLhs(name)) {
      return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      continue;
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
      // Check for self-referential property assignment
      if (childVal != null && isSelfReferential(propName, childVal, refs)) {
        return false;
      }
    }

    ret = true; // All checks passed, can be inlined
  }
  return ret;
}

// Helper method to determine if there's a self-referential property assignment
private boolean isSelfReferential(String propName, Node child, List<Reference> refs) {
  for (Reference ref : refs) {
    Node node = ref.getNode();
    if (node.isGetProp() && node.getLastChild().getString().equals(propName)) {
      return true; // Self-referential property assignment detected
    }
  }
  return false;
}