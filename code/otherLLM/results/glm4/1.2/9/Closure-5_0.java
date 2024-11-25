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
        validProperties.add(propName); // Add to the set of valid properties
      }
      continue; // No need to check further, as we already handled this property
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

      String childPropName = child.getString();
      if (!validProperties.contains(childPropName)) { // Check if the property is already valid
        validProperties.add(childPropName); // Add to the set of valid properties only if not already present
      }

      Node childVal = child.getFirstChild();
      if (childVal != null) {
        for (Reference t : refs) {
          Node refNode = t.getParent();
          while (!NodeUtil.isStatementBlock(refNode)) {
            if (refNode == childVal) {
              return false; // Self-referential assignment detected
            }
            refNode = refNode.getParent();
          }
        }
      }
    }

    ret = true;
  }
  return ret;
}