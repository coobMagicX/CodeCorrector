private boolean isInlinableObject(List<Reference> refs) {
  boolean ret = false;
  Set<String> validProperties = Sets.newHashSet();
  for (Reference ref : refs) {
    Node name = ref.getNode();
    Node parent = ref.getParent();
    Node gramps = ref.getGrandparent();

    // Handle indirection (property reads/writes)
    if (parent.isGetProp()) {
      Preconditions.checkState(parent.getFirstChild() == name);
      String propName = parent.getLastChild().getString();
      
      if (gramps.isDelProp() && gramps.getFirstChild() == parent) {
        // Property is deleted
        return false;
      }

      if (gramps.isCall() && gramps.getFirstChild() == parent) {
        // A call target that may be using the object as a 'this' value.
        return false;
      }
      
      if (NodeUtil.isVarOrSimpleAssignLhs(parent, gramps) && !ref.isLvalue()) {
        validProperties.add(propName);
      } else {
        return false;
      }
      continue;
    }

    // Handle var declarations and assignments
    if (!isVarOrAssignExprLhs(name)) {
      return false;
    }
    
    Node val = ref.getAssignedValue();
    if (val == null) {
      // A var with no assignment can't cause any harm
      continue;
    }

    if (!val.isObjectLit()) {
      return false; // Not an object literal
    }

    for (Node child = val.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isGetterDef() || child.isSetterDef()) {
        return false; // ES5 getters/setters not supported
      }
      
      Node childVal = child.getFirstChild();
      for (Reference t : refs) {
        Node refNode = t.getParent();
        while (!NodeUtil.isStatementBlock(refNode)) {
          if (refNode == childVal) {
            return false; // Self-referential
          }
          refNode = refNode.getParent();
        }
      }
      
      validProperties.add(child.getString());
    }

    ret = true; // Found a valid object literal assignment
  }
  return ret;
}
