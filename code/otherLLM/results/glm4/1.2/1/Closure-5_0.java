private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Ignore most indirect references, like x.y (but not x.y(),
    // since the function referenced by y might reference 'this').
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        return false;
      }

      String propName = parent.getLastChild().getString();
      if (!validProperties.contains(propName)) {
        // Check if the property is deleted or not defined on the object literal
        Node propDef = ref.getAssignedValue(); // This should ideally be an object literal assignment
        boolean isPropDefined = propDef != null && propDef.isObjectLit() && propDef.getString().equals(propName);
        if (!isPropDefined) {
          return false;
        }
        validProperties.add(propName);
      }
      continue;
    }

    // Only rewrite VAR declarations or simple assignment statements
    if (!isVarOrAssignExprLhs(name)) {
       return false;
    }

    Node val = ref.getAssignedValue();
    if (val == null) {
      // A var with no assignment.
      continue;
    }

    // We're looking for object literal assignments only
    if (!val.isObjectLit()) {
      return false;
    }

    // Check for self-referential assignments and ES5 getters/setters
    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false;
      }
      Node childVal = child.getFirstChild();
      if (isSelfReferential(childVal, refs)) {
        return false;
      }
    }

    // We have found an acceptable object literal assignment
    ret = true;
  }
  return ret;
}

private boolean isSelfReferential(Node node, List<Reference> refs) {
  for (Reference ref : refs) {
    Node parent = ref.getParent();
    while (!NodeUtil.isStatementBlock(parent)) {
      if (parent == node) {
        return true; // Self-referential assignment found
      }
      parent = parent.getParent();
    }
  }
  return false;
}